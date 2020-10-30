package scc.srv.api.controllers;

import scc.srv.api.MediaAPI;
import scc.srv.api.services.MediaService;

public class MediaController implements MediaAPI {

	private MediaService media;
	
	public MediaController() {
		this.media = new MediaService();
	}

	@Override
	public byte[] download(String id) {
		return media.download(id);
	}

	@Override
	public String upload(byte[] contents) {
		return media.upload(contents);
	}

	@Override
	public String delete(String id) {
		return media.delete(id);
	}
}
