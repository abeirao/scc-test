package scc.srv.api.controllers;

import java.util.List;

import scc.data.Entity;
import scc.srv.api.PageAPI;
import scc.srv.api.services.PageService;

public class PageController implements PageAPI {
	
	private PageService pages;
	
	public PageController() {
		this.pages = new PageService();
	}

	@Override
	public List<Entity> popularEntities() {
		return pages.popularEntities();
	}

	@Override
	public String listedEntity(String entityId) {
		return pages.listedEntity(entityId);
	}

	@Override
	public void setEntityLike(String entityId) {
		pages.setEntityLike(entityId);
	}

	@Override
	public String reservationStatistics() {
		return pages.reservationStatistics();
	}

}
