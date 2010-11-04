/*******************************************************************************
 * Copyright (c) 2010 Nicolas Roduit.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Nicolas Roduit - initial API and implementation
 ******************************************************************************/
package org.weasis.core.ui.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.weasis.core.api.gui.util.GuiExecutor;
import org.weasis.core.ui.Messages;
import org.weasis.core.ui.docking.UIManager;
import org.weasis.core.ui.editor.SeriesViewerFactory;

public class Activator implements BundleActivator, ServiceListener {

    private BundleContext bundleContext = null;
    private static final String pluginViewerFilter =
        String.format("(%s=%s)", Constants.OBJECTCLASS, SeriesViewerFactory.class.getName()); //$NON-NLS-1$

    // @Override
    public void start(final BundleContext bundleContext) throws Exception {
        this.bundleContext = bundleContext;
        bundleContext.addServiceListener(this, pluginViewerFilter);
        // must be instantiate in the EDT
        GuiExecutor.instance().execute(new Runnable() {

            public void run() {
                ServiceTracker m_tracker = new ServiceTracker(bundleContext, SeriesViewerFactory.class.getName(), null);
                m_tracker.open();
                Object[] services = m_tracker.getServices();
                for (int i = 0; (services != null) && (i < services.length); i++) {
                    if (!UIManager.SERIES_VIEWER_FACTORIES.contains(services[i])
                        && services[i] instanceof SeriesViewerFactory) {
                        UIManager.SERIES_VIEWER_FACTORIES.add((SeriesViewerFactory) services[i]);
                        // Activator.log(LogService.LOG_INFO, "Register viewer Plug-in: " + m_ref.toString());
                    }
                }
                // closing tracker will uregister services
                // m_tracker.close();
            }
        });
    }

    // @Override
    public void stop(BundleContext bundleContext) throws Exception {

    }

    public synchronized void serviceChanged(final ServiceEvent event) {
        // must be instantiate in the EDT
        GuiExecutor.instance().execute(new Runnable() {

            public void run() {
                ServiceReference m_ref = event.getServiceReference();
                SeriesViewerFactory viewerFactory = (SeriesViewerFactory) bundleContext.getService(m_ref);
                if (viewerFactory == null) {
                    return;
                }

                // TODO manage when several identical MimeType, register the default one
                if (event.getType() == ServiceEvent.REGISTERED) {
                    if (!UIManager.SERIES_VIEWER_FACTORIES.contains(viewerFactory)) {
                        UIManager.SERIES_VIEWER_FACTORIES.add(viewerFactory);
                        // Activator.log(LogService.LOG_INFO, "Register viewer Plug-in: " + m_ref.toString());
                    }
                } else if (event.getType() == ServiceEvent.UNREGISTERING) {
                    if (UIManager.SERIES_VIEWER_FACTORIES.contains(viewerFactory)) {
                        // Activator.log(LogService.LOG_INFO, "Unregister viewer Plug-in: " + m_ref.toString());
                        UIManager.SERIES_VIEWER_FACTORIES.remove(viewerFactory);
                        // Unget service object and null references.
                        bundleContext.ungetService(m_ref);
                    }
                }
            }
        });
    }

}