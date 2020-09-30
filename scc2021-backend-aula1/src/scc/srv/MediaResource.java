package scc.srv;

import scc.utils.Hash;

import java.io.File;
import java.nio.file.Files;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/media")
public class MediaResource {

	private static final String MEDIA_EXTENSION = "";
	private static final String ROOT_DIR = "/tmp/media/";

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	@Produces(MediaType.APPLICATION_JSON)
	public String upload(byte[] contents) {
		try {
			String id = Hash.of(contents);
			File filename = new File(ROOT_DIR + id + MEDIA_EXTENSION);

			if (filename.exists())
				return "conflict";

			Files.write(filename.toPath(), contents);
			return id;
		} catch (Exception x) {
			return "internal error";
		}
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public byte[] download(@PathParam("id") String id) {
		try {
			File filename = new File(ROOT_DIR + id + MEDIA_EXTENSION);
			if (filename.exists())
				return Files.readAllBytes(filename.toPath());
			else
				return null;
		} catch (Exception x) {
			return null;
		}
	}

}
