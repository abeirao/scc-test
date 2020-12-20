package scc.srv.api.controllers;

import java.io.IOException;

import javax.ws.rs.Path;

import scc.srv.api.MediaAPI;
import scc.srv.api.services.MediaService;

@Path(MediaAPI.ENDPOINT)
public class MediaController implements MediaAPI {

	private MediaService media;

	public MediaController() {
		this.media = new MediaService();
	}

	@Override
	public byte[] download(String id) {
		try {
			return media.download(id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String upload(byte[] contents) {
		return media.upload(contents);
	}

	@Override
	public String delete(String id) {
		try {
			return media.delete(id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
