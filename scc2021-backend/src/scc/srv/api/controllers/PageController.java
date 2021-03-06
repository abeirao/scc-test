package scc.srv.api.controllers;

import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Path;

import scc.data.Entity;
import scc.srv.api.PageAPI;
import scc.srv.api.services.PageService;

@Path(PageAPI.ENDPOINT)
public class PageController implements PageAPI {
	
	private PageService pages;
	
	public PageController() {
		this.pages = new PageService();
	}

	@Override
	public Iterator<Entity> popularEntities() {
		return pages.popularEntities();
	}

	@Override
	public boolean listedEntity(String entityId) {
		return pages.listedEntity(entityId);
	}

	@Override
	public Entity listEntity(String entityId) {
		return pages.listEntity(entityId);
	}


}
