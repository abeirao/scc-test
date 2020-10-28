package scc.srv;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import scc.srv.api.services.*;

/**
 * Cloud Computing Systems project - Reservation system
 * @author Henrique Realinho - number 50415
 *
 */
public class MainApplication extends Application
{
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> resources = new HashSet<Class<?>>();

	// add resources to resources set
	public MainApplication() { 
		resources.add(MediaService.class);
		resources.add(CalendarService.class);
		resources.add(EntityService.class);
		resources.add(ForumService.class);
		resources.add(ReservationService.class);
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
