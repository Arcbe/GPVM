package taiga.gpvm.event;

import taiga.code.registration.NamedSystem;
import taiga.gpvm.HardcodedValues;
import taiga.gpvm.schedule.WorldChange;
import taiga.gpvm.schedule.WorldChangeListener;
import taiga.gpvm.schedule.WorldUpdater;

/**
 * A class that takes events from a {@link WorldUpdater} and translates them into
 * more specific forms.  This allows event listeners for more selective events.
 * This class may use more than one {@link Thread} to process events, however 
 * each event for a given listener will occur sequential, but in an arbitrary
 * order.
 * 
 * @author russell
 */
public class MapEventManager extends NamedSystem implements WorldChangeListener {

  public MapEventManager() {
    super(HardcodedValues.MAP_EVENT_MANAGER_NAME);
  }

  @Override
  public void worldChanged(WorldChange change, Object prev) {
    
  }

  @Override
  protected void startSystem() {
  }

  @Override
  protected void stopSystem() {
  }

  @Override
  protected void resetObject() {
  }
}
