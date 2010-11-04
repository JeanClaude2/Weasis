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
package org.weasis.core.ui.graphic;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.weasis.core.ui.Messages;
import org.weasis.core.ui.graphic.model.AbstractLayer;
import org.weasis.core.ui.graphic.model.LayerModel;

/**
 * The Class DragLayer.
 * 
 * @author Nicolas Roduit
 */
public class DragLayer extends AbstractLayer {

    private static final long serialVersionUID = 8576601524359423997L;

    public DragLayer(LayerModel canvas1, int drawMode) {
        super(canvas1, drawMode);
    }

    public void addGraphic(Graphic graphic) {
        if (!graphics.contains(graphic)) {
            graphic.setSelected(false);
            graphics.add(graphic);
            graphic.setLayer(this);
            graphic.addPropertyChangeListener(pcl);
            // repaint(graphic.getRepaintBounds());
        }
    }

    @Override
    public void paint(Graphics2D g2, AffineTransform transform, AffineTransform inverseTransform, Rectangle bound) {
        for (int i = 0; i < graphics.size(); i++) {
            Graphic graphic = graphics.get(i);
            // only repaints graphics that intersects or are contained in the clip bound
            if (bound == null || bound.intersects(graphic.getRepaintBounds())) {
                graphic.paint(g2, transform);
            } else if (graphic.getLabelBound() != null) {
                Rectangle2D labelBound = graphic.getLabelBound();
                Point2D p = new Point.Double(labelBound.getX(), labelBound.getY());
                inverseTransform.transform(p, p);
                if (bound.intersects(new Rectangle((int) Math.floor(p.getX()), (int) Math.floor(p.getY()), (int) Math
                    .ceil(labelBound.getWidth()), (int) Math.ceil(labelBound.getHeight())))) {
                    // TODO repaint label separately
                    graphic.paint(g2, transform);
                }
            }
        }
    }

    /**
     * getGraphicsInArea
     * 
     * @param rect
     *            Rectangle
     * @return List
     */
    @Override
    public java.util.List getGraphicsSurfaceInArea(Rectangle rect) {
        ArrayList arraylist = new ArrayList();
        for (int j = graphics.size() - 1; j >= 0; j--) {
            Graphic graphic = graphics.get(j);
            // optimisation : d'abord check si le rectangle est dans le bounding box (beaucoup plus rapide que de
            // checker
            // sur shape directement)
            if (graphic.getBounds().intersects(rect)) {
                if (graphic.intersects(rect)) {
                    arraylist.add(graphic);
                }
            }
        }
        return arraylist;
    }

    @Override
    public java.util.List getGraphicsBoundsInArea(Rectangle rect) {
        ArrayList arraylist = new ArrayList();
        for (int j = graphics.size() - 1; j >= 0; j--) {
            Graphic graphic = graphics.get(j);
            if (graphic.getRepaintBounds().intersects(rect)) {
                arraylist.add(graphic);
            }
        }
        return arraylist;
    }

    /**
     * getGraphicsSurfaceInArea
     * 
     * @param pos
     *            Point
     * @return List
     */
    @Override
    public Graphic getGraphicContainPoint(Point pos) {
        for (int j = graphics.size() - 1; j >= 0; j--) {
            AbstractDragGraphic graphic = (AbstractDragGraphic) graphics.get(j);
            // optimisation : d'abord check si le rectangle est dans le bounding box (beaucoup plus rapide que de
            // checker
            // sur shape directement)
            if (graphic.getRepaintBounds().contains(pos)) {
                if (graphic.getArea().contains(pos) || graphic.getResizeCorner(pos) != -1) {
                    return graphic;
                }
            }
        }
        return null;
    }

    public static String getDrawinType(AbstractDragGraphic graphic) {
        if (graphic instanceof LineGraphic) {
            return "Segment"; //$NON-NLS-1$
        } else if (graphic instanceof CircleGraphic) {
            return "Ellipse"; //$NON-NLS-1$
        } else if (graphic instanceof RectangleGraphic) {
            return "Rectangle"; //$NON-NLS-1$
        } else if (graphic instanceof PolygonGraphic) {
            return "Polygon"; //$NON-NLS-1$
        } else {
            return "FreeHand"; //$NON-NLS-1$
        }
    }
}