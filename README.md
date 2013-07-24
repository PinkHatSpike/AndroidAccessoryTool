AndroidAccessoryTool
====================

Processing Tool to twiddle XML build files for Android OpenAccessory (ADK) support.

This tool will modify a sketch's AndroidManifest.xml file and create a accessory_filter.xml file to add Accessory support to the sketch. Note that this does not take care to communication with an accessory. It only registers the sketch with the android device as able to work with a specific Model, Manufacturer, and Version of an accessory.

For the actual OpenAccessory protocol communication part, check out [pioio](https://github.com/PinkHatSpike/pioio) or AndroidAccessory library for Processing (COMING SOON)

Heavily borrows from [TellArt's Arduino_ADK Tool](http://stream.tellart.com/controlling-arduino-with-android/).

built on top of
* [Processing](http://processing.org/) 2.0

Check out the [Processing Android mode](http://wiki.processing.org/w/Android) and the official [Android OpenAccessory](http://developer.android.com/tools/adk/index.html) documentation.

Install
-------
1. Download the [latest master zip](https://github.com/PinkHatSpike/AndroidAccessoryTool/archive/master.zip).
2. Rename downloaded zip file to AndroidAccessoryTool.zip, and unzip it.
3. Copy folder to your sketchbook's tool folder (similar to how a library is hand installed, see [How to Install a Contributed Library](http://wiki.processing.org/w/How_to_Install_a_Contributed_Library)).

How to use
----------
1. Run your sketch at least once in Android mode (to create an initial AndroidManifest.xml).
2. From the Processing menu, open Tools -> Android Accessory Tool.
3. Fill in the Model, Manufacturer, and Version fields (fields can be left blank as needed) OR select a preset from the drop-down menu.
4. Click "Write XML files" to write out the AndroidManifest.xml and accessory_filter.xml files.

Accessory support can be removed from your sketch by clicking the "Remove Accessory Support" button.

License
-------
AndroidAccessoryTool is licensed under [LGPL 3.0](http://www.gnu.org/licenses/lgpl-3.0.html)

<pre>
/*
  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation, version 3.0.
  (http://www.gnu.org/licenses/lgpl-3.0.html)

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General
  Public License along with this library; if not, write to the
  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA  02111-1307  USA
  
  Contributors:
      PinkHatSpike - spike@pinkhatproductions.com
      
  Heavily borrows from [TellArt's Arduino_ADK Tool](http://stream.tellart.com/controlling-arduino-with-android/)
*/
</pre>

AndroidAccessoryTool is built on top of [Processing](https://github.com/processing/processing) which carries its own license and terms.