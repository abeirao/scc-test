package scc.srv.api.controllers;

import java.util.List;

import scc.data.Entity;
import scc.srv.api.PageAPI;
import scc.srv.api.services.PageService;

public class PageController implements PageAPI {
	
	private PageService pages;
	
	public PageController() {
		this.pages = pages;
	}

	@Override
	public List<Entity> popularEntities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String listedEntity(String entityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEntityLike(String entityId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String reservationStatistics() {
		// TODO Auto-generated method stub
		return null;
	}

}
