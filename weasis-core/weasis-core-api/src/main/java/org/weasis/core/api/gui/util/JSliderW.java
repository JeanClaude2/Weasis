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
package org.weasis.core.api.gui.util;

import javax.swing.BoundedRangeModel;
import javax.swing.JSlider;

public class JSliderW extends JSlider {

    private int labelDivision = 4;
    private boolean displayValueInTitle = true;
    private boolean showLabels = true;

    public JSliderW(BoundedRangeModel brm) {
        super(brm);
    }

    public JSliderW(int orientation, int min, int max, int value) {
        super(orientation, min, max, value);
    }

    public JSliderW(int min, int max, int value) {
        super(min, max, value);
    }

    public JSliderW(int min, int max) {
        super(min, max);
    }

    public JSliderW(int orientation) {
        super(orientation);
    }

    public int getLabelDivision() {
        return labelDivision;
    }

    public void setLabelDivision(int labelDivision) {
        this.labelDivision = labelDivision;
    }

    public boolean isdisplayValueInTitle() {
        return displayValueInTitle;
    }

    public void setdisplayValueInTitle(boolean displayOnlyValue) {
        this.displayValueInTitle = displayOnlyValue;
    }

    public boolean isShowLabels() {
        return showLabels;
    }

    public void setShowLabels(boolean showLabels) {
        this.showLabels = showLabels;
    }

}
