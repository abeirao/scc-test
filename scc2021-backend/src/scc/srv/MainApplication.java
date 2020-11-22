package scc.srv;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import scc.srv.api.*;
import scc.srv.api.services.*;
import scc.srv.api.controllers.*;

/**
 * Cloud Computing Systems project - Reservation system
 * @author Henrique Realinho - number 50415
 *
 */
@ApplicationPath("")
public class MainApplication extends Application
{

	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> resources = new HashSet<Class<?>>();

	// add resources to resources set
	public MainApplication() { 
		resources.add(ControlResource.class);
		resources.add(MediaController.class);
		resources.add(CalendarController.class);
		resources.add(Entities.class);
		resources.add(EntityController.class);
		resources.add(ForumController.class);
		resources.add(ReservationController.class);
	}

	@Override
	public Set<Class<?>> getClasses() {
		return resources;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	} 
}
