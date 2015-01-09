package edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.digest.DigestUtils;

import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.Comentario;
import edu.upc.eetac.dsa.dsaqt2014g1.blackmarket.api.model.User;

@Path("/users")
public class UserResource {
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	 
	private final static String GET_USER_BY_USERNAME_QUERY = "Select * from users where username=?";
	private final static String INSERT_USER_INTO_USERS = "Insert into users values(?, MD5(?), ?, ?)";
	private final static String INSERT_USER_INTO_USER_ROLES = "Insert into user_roles values (?, 'registered')";
	private final static String UPDATE_USUARIO_QUERY="update users set nombre=ifnull(?, nombre),email=ifnull(?, email) where username=?";
	private final static String GET_ROL_BY_USERNAME_QUERY = "Select rolename from user_roles where username=?";
	
	private final static String DELETE_USER_QUERY = "Delete from users where username=?";
	

	private User getUserFromDatabaseNopassword(String username) {
		User usuario = new User();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_USER_BY_USERNAME_QUERY);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				usuario.setUsername(rs.getString("username"));
				usuario.setEmail(rs.getString("email"));
				usuario.setNombre(rs.getString("nombre"));
			} else {
				throw new NotFoundException(username+ " no encontrado!");
			}
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return usuario;
	}

	
	@GET
	@Path("/{username}")
	@Produces(MediaType2.BLACKS_API_USER)
	public Response getSting(@PathParam("username") String username, @Context Request request) {
		User usuario = new User();
		CacheControl cc = new CacheControl();
		usuario = getUserFromDatabaseNopassword(username);		
		String referencia = DigestUtils.md5Hex(usuario.getNombre()+ usuario.getEmail());
		EntityTag eTag = new EntityTag(referencia);
		Response.ResponseBuilder rb = request.evaluatePreconditions(eTag); 
		if (rb != null) {
			return rb.cacheControl(cc).tag(eTag).build();
		}
		rb = Response.ok(usuario).cacheControl(cc).tag(eTag);	 
		return rb.build();
	}
	
	
	@POST
	@Consumes(MediaType2.BLACKS_API_USER)
	@Produces(MediaType2.BLACKS_API_USER_COLLECTION)
	public User createUser(User user) {
		validateUser(user);
 
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		PreparedStatement stmtGetUsername = null;
		PreparedStatement stmtInsertUserIntoUsers = null;
		PreparedStatement stmtInsertUserIntoUserRoles = null;
		try {
			stmtGetUsername = conn.prepareStatement(GET_USER_BY_USERNAME_QUERY);
			stmtGetUsername.setString(1, user.getUsername());
 
			ResultSet rs = stmtGetUsername.executeQuery();
			if (rs.next())
				throw new WebApplicationException(user.getUsername()
						+ " already exists.", Status.CONFLICT);
			rs.close();
 
			conn.setAutoCommit(false);
			stmtInsertUserIntoUsers = conn
					.prepareStatement(INSERT_USER_INTO_USERS);
			stmtInsertUserIntoUserRoles = conn
					.prepareStatement(INSERT_USER_INTO_USER_ROLES);
 
			stmtInsertUserIntoUsers.setString(1, user.getUsername());
			stmtInsertUserIntoUsers.setString(2, user.getPassword());
			stmtInsertUserIntoUsers.setString(3, user.getNombre());
			stmtInsertUserIntoUsers.setString(4, user.getEmail());
			stmtInsertUserIntoUsers.executeUpdate();
 
			stmtInsertUserIntoUserRoles.setString(1, user.getUsername());
			stmtInsertUserIntoUserRoles.executeUpdate();
 
			conn.commit();
		} catch (SQLException e) {
			if (conn != null)
				try {
					conn.rollback();
				} catch (SQLException e1) {
				}
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmtGetUsername != null)
					stmtGetUsername.close();
				if (stmtInsertUserIntoUsers != null)
					stmtGetUsername.close();
				if (stmtInsertUserIntoUserRoles != null)
					stmtGetUsername.close();
				conn.setAutoCommit(true);
				conn.close();
			} catch (SQLException e) {
			}
		}
		user.setPassword(null);
		return user;
	}
	
	
	@DELETE
	@Path("/{username}")
	public void deleteSting(@PathParam("username") String username) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(DELETE_USER_QUERY);
			stmt.setString(1, username);

			int rows = stmt.executeUpdate();
			if (rows == 0)
				throw new NotFoundException("No existe ningun usuario con el nombre de usuario "
						+ username);
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
	}
 	
	private void validateUser(User user) {
		if (user.getUsername() == null)
			throw new BadRequestException("username cannot be null.");
		if (user.getPassword() == null)
			throw new BadRequestException("password cannot be null.");
		if (user.getNombre() == null)
			throw new BadRequestException("name cannot be null.");
		if (user.getEmail() == null)
			throw new BadRequestException("email cannot be null.");
	}
 
	@Path("/login")
	@POST
	@Produces(MediaType2.BLACKS_API_USER)
	@Consumes(MediaType2.BLACKS_API_USER_COLLECTION)
	public User login(User user) {
		if (user.getUsername() == "" || user.getPassword() == "")
			throw new BadRequestException(
					"Los campo username y contraseña no pueden ser nulos.");
 
		String pwdDigest = DigestUtils.md5Hex(user.getPassword());
		String storedPwd = getUserFromDatabase(user.getUsername(), true)
				.getPassword();
 
		user.setLoginSuccessful(pwdDigest.equals(storedPwd));
		String rol = getRolFromDatabase(user.getUsername());
		user.setRol(rol);
		user.setPassword(null);
		return user;
	}
 
	private User getUserFromDatabase(String username, boolean password) {
		User user = new User();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
 
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_USER_BY_USERNAME_QUERY);
			stmt.setString(1, username);
 
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				user.setUsername(rs.getString("username"));
				if (password)
					user.setPassword(rs.getString("password"));
				user.setEmail(rs.getString("email"));
				user.setNombre(rs.getString("nombre"));
			} else
				throw new NotFoundException(username + " no registrado.");
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
 
		return user;
	}
	
	private String getRolFromDatabase(String username) {
		String rol ;
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
 
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_ROL_BY_USERNAME_QUERY);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				rol= rs.getString("rolename");
			} else
				throw new NotFoundException(username + " no registrado.");
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
				
				//FALTA ALGO
			}
		}
		return rol;
	}
	
	
	@PUT
	@Path("/{username}")
	@Consumes(MediaType2.BLACKS_API_USER)
	@Produces(MediaType2.BLACKS_API_USER)
	public User updateUsuario(
			@PathParam("username") String username,
			User user) {
		// validateUser(stingid);
		validateUpdateUsuario(user);
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(UPDATE_USUARIO_QUERY);
			stmt.setString(1, user.getNombre());
			stmt.setString(2, user.getEmail());
			stmt.setString(3, username);

			int rows = stmt.executeUpdate();
			if (rows == 1)
				user = getUserFromDatabaseNopassword(username);
			else {
				throw new NotFoundException(
						"There's no user with username=" + username);
			}

		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return user;
	}

	private void validateUpdateUsuario(User user) {
		if (user.getNombre() != null
				&& user.getNombre().length() > 50)
			throw new BadRequestException(
					"Comentario can't be greater than 50 characters.");
		if (user.getEmail() != null
				&& user.getEmail().length() > 50)
			throw new BadRequestException(
					"Comentario can't be greater than 50 characters.");

	}
	


}
