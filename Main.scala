import unfiltered.jetty._
import unfiltered.request._
import unfiltered.response._


object Main extends App{
	Http.local(8080).filter(Cake.UserController).run()
}

object Cake extends
   UserContollerLayer
   with UserRepositoryMapImpl

trait UserContollerLayer extends UserRepositoryLayer {

	object UserController extends unfiltered.filter.Plan {
		def intent = {
			case Path(Seg("user" :: id :: Nil)) => userRepository.get(id) match {
				case Some(User(_, name)) => ResponseString(name)
				case _ => NotFound
			}
		}
}

}

case class User(id: String, name: String)

trait UserRepository{
	def get(id: String): Option[User]
}

trait UserRepositoryLayer{
	val userRepository: UserRepository
}


trait UserRepositoryMapImpl extends UserRepositoryLayer{

	override object userRepository extends UserRepository{
		private val users = Map[String, User](
			"dustin" -> User("dustin", "Dustin Whitney"))

		def get(id: String): Option[User] = users.get(id)
	}
}