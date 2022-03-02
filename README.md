# 2D-editor-Objects-and-transformations-

This program is for Homework 1.2 - 2D editor (Objects and transformations)


# 1. Selection #

============

It should be possible for at least one object to be selected. Designate one of your implemented materials to highlight selected objects. In addition to rendering those objects regularly, also render them with the designated material. [Note that you can override the wired-in material settings of a game object with myGameObject.using(selectedMaterial).draw()] The most straightforward way for implementing selection is adding a selectedGameObjects collection property to Scene, in addition to gameObjects. A game object can appear in both collections. After all of gameObjects have been drawn, selectedGameObjects can be drawn over them.

Space pick

--------

Pressing the space bar should change which object (or objects) are selected.




# 2. Position manipulation #

========================

The user should be able to edit object positions. Specifically, the position of selected objects should be affected. Thus, some sort of selection being present is a prerequisite for these items.

Mouse drag [M] (*)

----------

Change positions of the selected objects if the mouse button is kept pressed while moving the mouse. The translation should correspond to the mouse offset, effectively dragging the objects. 




# 3. Orientation manipulation #

===========================

The user should be able to edit object orientations. Specifically, the orientation of selected objects should be affected. Thus, some sort of selection being present is a prerequisite for these work items.

Mouse rotate [M]  (*)

------------

Change orientations of the selected objects if the mouse button is kept pressed while moving the mouse. The rotation should correspond to the angular mouse delta (atan2(newMousePos - pivot) - atan2(oldMousePos - pivot)), with the position of the first selected object as the rotation pivot.




# 4. Editing #

==========



Delete

------

Selected objects should be removed if 'DEL' is pressed.




# 5. View #

=======



Zoom

----

Pressing 'Z' should zoom in, pressing 'X' should zoom out.




# 6. Combo #

========



Camera & mouse

--------------

This requires Zoom, Scroll, or Pan, and also Mouse pick, Mouse drag, or Mouse rotate. Make sure the mouse operations work even when the camera settings have changed by transforming mouse coordinates from normalized device coordinates to word space coordinates using the inverse of the camera's view-projection matrix.




(*):  these modes are switched when the 'Enter' key is pressed.

Reference: 
László Szécsi. "Homework 1.2: 2D editor (Objects and transformations)". _CORA (Community of Online Research Assignments)_. 2021. Web. Saturday, 16 October 2021.
