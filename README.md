bit-bin
=======

Code and stuff

What's what....

All code currently under bit-bin is java code dealing with Slick2D. Either games or game/graphic related programs.

SystemOverload is the project that got me started with Slick2D, it was developed for Bryant York's
CS313 class Fall of 2012. It is the most complete project there but since it was developed with tight time constraints
I'm not exactly proud of some of the slop. It contains a modified version of A-Star that factors in Manhattan blocks
and multiple starting points, destinations as well as cost savings by short-cuts.

ParticleTest is a test program for the Particle System Emitter in Slick2D. There are better programs out there but I
got good at making graphics only buttons and thought I'd make a pure graphics editor. Slick has a GUI templating
system called TWL that lets you use basic Java UI building objects but gives them a graphical look. So I'd check
there for GUI inspiration than looking at my code.

SpaceJunkie is a starter for a 2D spaceship game, it uses ParticleSystem Emitters for space and exhaust and has
some decent mathematics for movements as well as a crude gimbal system which is based on a rectangle when it should
be based on a circle (edge navigation is clunky).

Animations will someday have an actual animation. For now its an isometric tile renderer that I am working on making
a good buffering algorithm for. When that is done I'll add an animated guy walking around at center.

ParticleSystemGarden is meant to be a bunch of ParticleSystem eyecandy. Essentially a bunch of different emitters
all doing stuff. Its pretty empty as a project so ignore that until you see recent (post 03/13) checkins. 
