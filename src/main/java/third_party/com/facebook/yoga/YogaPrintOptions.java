package third_party.com.facebook.yoga;

/**
 * Copyright (c) 2014-present, Facebook, Inc.
 * Copyright (c) 2018-present, Marius Klimantaviius
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

public enum YogaPrintOptions {

    Layout(1),
    Style(2),
    Children(4);

    public final int bit;

    private YogaPrintOptions(int bit) {
        this.bit = bit;
    }
}
