/*
 * Copyright 2014 Shazam Entertainment Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.shazam.fork.pooling;

import com.shazam.fork.model.Pool;
import com.shazam.fork.model.Devices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Invokes selected device pool loaders for obtaining connected devices.
 */
public class CompositeDevicePoolLoader implements DevicePoolLoader {
    private static final Logger logger = LoggerFactory.getLogger(CompositeDevicePoolLoader.class);
    private final Collection<DevicePoolLoader> devicePoolLoaders;

    public CompositeDevicePoolLoader(Collection<DevicePoolLoader> devicePoolLoaders) {
        this.devicePoolLoaders = devicePoolLoaders;
    }

	@Override
	public Collection<Pool> loadPools(Devices devices) {
        Collection<Pool> pools = new ArrayList<>();
		for (DevicePoolLoader devicePoolLoader : devicePoolLoaders) {
			pools = devicePoolLoader.loadPools(devices);
			if (!pools.isEmpty()) {
                logger.info("Picked {}", devicePoolLoader.getClass().getSimpleName());
				break;
			}
		}
		log(pools);
		return pools;
	}

    private void log(Collection<Pool> configuredPools) {
        logger.info("Number of device pools: " + configuredPools.size());
		for (Pool pool : configuredPools) {
            logger.debug(pool.toString());
		}
	}
}
