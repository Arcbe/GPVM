/*
 * Copyright (C) 2014 Russell Smith.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

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
    super(HardcodedValues.NAME_MAP_EVENT_MANAGER);
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
