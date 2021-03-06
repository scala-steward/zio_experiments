package io.abp.users.interfaces.http

import dev.profunktor.tracer.auth.{Http4sAuthTracerDsl, AuthTracedHttpRoute}
import dev.profunktor.tracer.Tracer
import io.abp.users.domain.User
import io.abp.users.interfaces.http.UsersRoutes._
import io.abp.users.programs.UserProgram
import io.abp.users.programs.UserProgram.ProgramError
import io.abp.users.services.users.UserService
import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import org.http4s._
import org.http4s.circe.CirceEntityDecoder.circeEntityDecoder
import org.http4s.circe.CirceEntityEncoder.circeEntityEncoder
import zio._
import zio.interop.catz._
import zio.telemetry.opentracing.OpenTracing

class UsersRoutes[Env: Tag](implicit tracer: Tracer[AppTask[Env, ?]]) {
  val dsl = Http4sAuthTracerDsl[AppTask[Env, ?]]
  import dsl._

  val routes = AuthTracedHttpRoute[Challenge, AppTask[Env, ?]] {
    case GET -> Root as challenge using traceId =>
      challenge.params.get("token") match {
        case Some("dolphins") => Forbidden("dolphins are not allowed")
        case None             => BadRequest() //Unauthorized would be better but doesn't compile...
        case _ =>
          UserProgram.getAllUsers
            .foldM(errorHandler, users => Ok(AllUsersResponse(users)))
            .root(s"$traceId UserRoutes - Get All Users")
      }

    case GET -> Root / id as _ using traceId =>
      UserProgram
        .getUser(User.Id(id))
        .foldM(errorHandler, user => Ok(GetUserResponse(user)))
        .root(s"$traceId UserRoutes - Get User")

    case tr @ POST -> Root as _ using traceId =>
      tr.request.req.as[CreateUserRequest].flatMap { req =>
        UserProgram
          .createUser(req.name)
          .foldM(
            errorHandler,
            id => Ok(CreateUserResponse(id))
          )
          .root(s"$traceId UserRoutes - Create User")
      }
  }

  //TODO: improve error handling
  private def errorHandler: ProgramError => AppTask[Env, Response[AppTask[Env, ?]]] = {
    case ProgramError.UserAlreadyExists => Conflict("User already exists")
    case ProgramError.UserError(_)      => InternalServerError()
    case ProgramError.ConsoleError(_)   => InternalServerError()
    case ProgramError.ClockError(_)     => InternalServerError()
  }

}

object UsersRoutes {
  def apply[Env: Tag](implicit tracer: Tracer[AppTask[Env, ?]]): UsersRoutes[Env] =
    new UsersRoutes[Env]
  type AppTask[Env, A] = RIO[Env with UserService[Env] with OpenTracing, A]
  val PathPrefix = "/users"

  final case class AllUsersResponse(users: List[User])
  final case class GetUserResponse(user: Option[User])
  final case class CreateUserRequest(name: String)
  final case class CreateUserResponse(id: User.Id)

  implicit val userIdEncoder: Encoder[User.Id] = deriveEncoder[User.Id]
  implicit val userEncoder: Encoder[User] = deriveEncoder[User]
  implicit val allUsersRespEncoder: Encoder[AllUsersResponse] = deriveEncoder[AllUsersResponse]
  implicit val createUserRespEncoder: Encoder[CreateUserResponse] =
    deriveEncoder[CreateUserResponse]
  implicit val getUserRespEncoder: Encoder[GetUserResponse] = deriveEncoder[GetUserResponse]

  implicit val createUserReqDecoder: Decoder[CreateUserRequest] = deriveDecoder[CreateUserRequest]

}
