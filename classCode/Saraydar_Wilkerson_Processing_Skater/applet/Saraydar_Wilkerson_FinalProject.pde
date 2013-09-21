import ddf.minim.*;
AudioPlayer song;
AudioPlayer hit;
Minim minim;
PImage gatorMenu;
PImage turlingtonFloor;
PImage GatorClose;
PFont font;
boolean menu = true;
boolean playing = true;
boolean  game=false;
boolean end=false;
Screen screens;
Timer timerW;
Timer timerB;
Timer timerG;
BikerDude[] biker;
WalkingDude[]  walker;
Gator[] gator;
CircleMan skater;
int tempspeed =0;
int totalWalkers = 0;
int totalBikers = 0;
int totalGators = 0;
int walk=0;
int gatorChomp=0;
int time;
int score=0;
boolean WalkerArrayFull=false;
boolean BikerArrayFull=false;
boolean GatorArrayFull=false;


void setup () {
  minim = new Minim(this);
// load song from the data folder
  song = minim.loadFile("Funk.mp3");
  hit = minim.loadFile("watchOut.wav");
  //load all  image files
 gatorMenu = loadImage( "gator.jpg");
 turlingtonFloor = loadImage("TurlingtonFloor.jpg");
 GatorClose = loadImage("gatorChar.gif");
  size(600,600);
  smooth();
  font = loadFont("BankGothicBT-Light-48.vlw");
  screens = new Screen();
   biker = new BikerDude[20];    // Create 20 spots in the array
   walker = new WalkingDude[50]; // Create 50 walkingdudes in the array 
   gator = new Gator[20];         // Create 20 gators in the array
   timerW = new Timer(300);   // Create a timer that goes off every 300 milliseconds
   timerB = new Timer(600);   // Create a timer that goes off every 600 milliseconds
   timerG = new Timer((int) random(2500,5000)); // Create a timer that goes off 2500-5000 milliseconds
   skater = new CircleMan(10,600,300);
   timerW.start();
   timerB.start();
   timerG.start();
}

void stop(){
  hit.close();
  song.close();
  minim.stop();
  super.stop();
}

void draw() {
 if (playing) {
   //define the current screen and reset settings
  if (menu){
    background(34,31,160);
    image (gatorMenu,0,80);
    screens.startMenu();
    time =30;
    score=0;
    skater.setLocation(600,300);
   
  }
  if (game){
    song.play();
    screens.gameScreen();
    skater.display();
    skater.keyPressed();
    skater.reset();
    for (int i = 0; i < totalWalkers; i++ ) {
      if (skater.intersect1(walker[i])) {
        skater.collide();
        hit.play();
        hit.rewind();
      }
    }
    for (int j = 0; j < totalGators; j++ ) {
      if (skater.intersect2(gator[j])){
        skater.collide();
        hit.play();
        hit.rewind();
      }
    }
    for (int k = 0; k < totalBikers; k++ ) {
      if(skater.intersect3(biker[k])){
        skater.collide();
        hit.play();
        hit.rewind();
      }
    }
  }
  if(end){
    screens.endMenu();
    screens.keyReleased();
  }
 }
}
void mouseReleased(){
   if (mouseX>=80 && mouseX<=270 && mouseY>=400 && mouseY<=480) {
      println("Level 1");
      menu=false;
      game=true;
      end=false;
      int totalWalkers = 0;
      int totalBikers = 0;
      int totalGators = 0;
      tempspeed=1;
   }
   if (mouseX>=310 && mouseX<=510 && mouseY>=400 && mouseY<=480) {
     println("Level 2");
      menu=false;
      game=true;
      end= false;
      int totalWalkers = 0;
      int totalBikers = 0;
      int totalGators = 0;
      tempspeed=2;
   }
   if ((mouseX>=500 && mouseX<=580 && mouseY>=5 && mouseY<=45)|| time==0) {
     println("Quit");
        menu=false;
        game=false;
        end=true;
        
     }
     if(mouseX>=240 && mouseX<=360 && mouseY>=500 && mouseY<=560){
          background(0);
         textAlign(CENTER);
         fill(255,49,8);
         textFont(font,50);
         text("Peace Out!!!",300,125);
         playing=false;
     }
    }
class CircleMan {
  float x,y,z;
  
  CircleMan(float tempZ, float tempX, float tempY) {
     x = tempX;
     y = tempY;
     z = tempZ;
   }
  
 void setLocation(float tempX, float tempY) {
   x = tempX;
   y = tempY;
 }


 void display() {
    rectMode(CENTER);
    fill(124,115,115);
    noStroke();
    ellipse(x-25,y,z+5,z+2);
    ellipse(x+25,y,z+5,z+2);
    rect(x,y,z+35,z+2);
    stroke(1);
    fill(53,41,240);
    ellipse(x-4,y,z+15,z-2);
    ellipse(x+4,y,z+15,z-2);
    fill(255,137,10);
    rect(x-10,y+5,z-10,z+5);
    rect(x+10,y+5,z-10,z+5);
    rect(x,y+10,z+10,z-5);
    fill(53,41,240);
    ellipse(x,y,z+5,z+5);
 } 
 void reset() {
   if (x==0){
     x=600;
     score++;
   }
 }
   void collide() {
     x=600;
 }
 //skaters interactive movement
 void keyPressed() {
   if(key==CODED) {
     if(keyCode == UP) {
       y-=3;
       if (y<55) {
         y=55;
       }
     }
     else if(keyCode == DOWN) {
       y+=3;
       if (y>(height-5)) {
         y=height-5;
       }
     }
     else if (keyCode == RIGHT) {
       x+=3;
       if (x>width){
         x=width-15;
       }
     }
     else if (keyCode == LEFT) {
       x-=3;
       if (x<0) {
         x=15;
       }
     }
   }
 }
// A function that returns true or false if the CircleMan is hit
// by the Gator, walkingDude, or BikerDude
   boolean intersect1(WalkingDude w) {
    float distanceW = dist(x,y,w.x,w.y);
    if (distanceW < z + w.q) {
      return true;
    } else {
      return false;
    }
  }
   boolean intersect2 (Gator g) { 
     //3 circles represent skater to make collition with obtuce object of gator more accurate
    float distanceG = dist(x,y,(g.x)+(GatorClose.width/2),(g.y)+(GatorClose.height/2));
    if (distanceG < z + g.r) {
      return true;
    }
    distanceG = dist(x+30,y,(g.x)+(GatorClose.width/2),(g.y)+(GatorClose.height/2));
    if (distanceG < z + g.r) {
      return true;
    }
    distanceG = dist(x-30,y,(g.x)+(GatorClose.width/2),(g.y)+(GatorClose.height/2));
    if (distanceG < z + g.r) {
      return true;
    }
      return false;
  }
  boolean intersect3 (BikerDude b) {
    float distanceB = dist(x,y,b.x,b.y);
    if (distanceB < z + b.r) {
      return true;
    } else {
      return false;
    }
  }
}
//----------------------------
class WalkingDude {
  float x,y;   // Variables for location of raindrop
  float speed; // Speed of WalkingDude
  color r, g, b, HairColor;
  float q; // personal space bubble

  WalkingDude() {
    x = -15;     // Start a little to the left of the window
    y = random(60,height);      //start at a random height below 100        
    speed =4*tempspeed;   // Pick a random speed
    r=(int) random(255);
    g=(int) random(255);
    b=(int) random(255);
    HairColor=(int) random(4);
    q=5;
  }

  // Move the walkingDude down
  void move() {
    walk++;
    // Increment by speed
    x += speed; 
  }

  // Display the WalkingDude
  void display() {    
    if(walk%2==0){
      pushMatrix();
      fill(255);
      rect(x-3, y-6, 5, 3);
      rect(x+3, y+6, 5, 3);
      fill(r, g, b);
      ellipse(x, y, 5, 20);
      ellipse(x-5, y-6, 3, 3);
      ellipse(x+5, y+6, 3, 3);
      switch (HairColor){
        case 1: fill(200,200,25); break;
        case 2: fill(200,150,100); break;
        case 3: fill(0,0,0); break;
        case 4: fill(200,75,10); break;
        default: fill(200,150,100); break;
      }
      ellipse(x, y, 10, 10);
      noFill();
      ellipse(x,y,q*2,q*4);
      popMatrix();
    }
    else {
      pushMatrix();
      fill(255);
      rect(x+3, y-6, 5, 3);
      rect(x-3, y+6, 5, 3);
      fill(r, g, b);
      ellipse(x, y, 5, 20);
      ellipse(x+5, y-6, 3, 3);
      ellipse(x-5, y+6, 3, 3);
      switch (HairColor){
        case 1: fill(200,200,25); break;
        case 2: fill(200,150,100); break;
        case 3: fill(0,0,0); break;
        case 4: fill(200,75,10); break;
        default: fill(200,150,100); break;
      }
      ellipse(x, y, 10, 10);
      noFill();
      ellipse(x,y,q*2,q*4);
      popMatrix();
    }
  }
}
//----------------------------
class BikerDude {
  float x,y;   // Variables for location of biker
  float speed; // Speed of biker
  color Rd, g, b, HairColor;
  float r; // personal space bubble

  BikerDude() {
    x = -15;     // Start with a random x location
    y = random(60,height);  // Start a little above the window
    speed = 7*tempspeed;   // Pick a random speed
    r = 5;
    Rd=(int) random(255);
    g=(int) random(255);
    b=(int) random(255);
    HairColor=(int) random(4);
  }

  // Move the biker across the screen
  void move() {
    // Increment by speed
    x += speed; 
  }

  // Display the BikerDude
  void display() {
    // Display the BikerDude
    rectMode(CENTER);
    fill(0);
    rect(x, y, 25, 3);
    rect(x+10, y, 1, 10);
    fill(Rd,g,b);
    rect(x+5, y-7, 10, 3);
    rect(x+5, y+7, 10, 3);
    ellipse(x, y, 5, 20);
    ellipse(x+10, y-7, 3, 3);
    ellipse(x+10, y+7, 3, 3);
      switch (HairColor){
        case 1: fill(200,200,25); break;
        case 2: fill(200,150,100); break;
        case 3: fill(0,0,0); break;
        case 4: fill(200,75,10); break;
        default: fill(200,150,100); break;
      }
    ellipse(x, y, 10, 10);
    noFill();
    noStroke();
    ellipse(x,y,r*5,r*4);
    stroke(1);
  }
}
class Gator{
  float x,y;   // Variables for location of the gator
  float speed; // Speed of gator
  color c;
  float r;//personal space bubble

  Gator() {
   
    x = -15;     // Start with a random x location
    y = random(75,height-50);              // Start a little above the window
    speed = 1*tempspeed;   // Pick a random speed
    r=20;
  }

  // Move the gator across screen
  void move() {
    // Increment by speed
    x += speed; 
  }

  // Display the Gator
  void display() {
      image (GatorClose, x,y);
      noFill();
      noStroke();
      ellipse(x+(GatorClose.width/2),y+(GatorClose.height/2),r*4, r*2);
      stroke(1);
  }
}
//------------------------------
class Screen {
  int x,y;

 Screen () {
  x=100;
  y=100;
 }
      //---------Start Menu----------// 
 void startMenu () {
   textFont(font,100);
  textAlign(CENTER);
  fill(255);
  text("Turlington", 300,60);
  text("Turmoil",300,585);
  textFont(font,50);
  fill(255,49,8);
  text("Level 1",180,450);
  text("Level 2",410,450);
  
  if (mouseX>=80 && mouseX<=270 && mouseY>=400 && mouseY<=480) {
    strokeWeight(3);
    fill(255,243,106);
    text("Level 1",180,450); 
    strokeWeight(1);
  } else {
    strokeWeight(1);
    fill(255,49,8);
    text("Level 1",180, 450); 
  }
  if (mouseX>=310 && mouseX<=510 && mouseY>=400 && mouseY<=480) {
    strokeWeight(3);
    fill(255,243,106);
    text("Level 2",410,450);
    strokeWeight(1);  
  } else {
    strokeWeight(1);
    fill(255,49,8);
    text("Level 2",410,450); 
  }
  textFont(font, 50);
  text("QUIT", 300, 525);
  if (mouseX>=240 && mouseX<=360 && mouseY>=500 && mouseY<=560) {
    strokeWeight(3);
    fill(255,243,106);
    text("QUIT",300,525);
    strokeWeight(1);
    stroke(0);
  } else {
    fill(255,49,8);
    text("QUIT",300,525);    
  }
 }
      //---------GameScreen----------//
 void gameScreen(){
  song.play();
  background(0);
  time--;
  fill(0, 200, 0);
  textFont(font,40);
  text("Score:", 80,40);
  text(score, 220, 40);
  text((time/60)+30, 400, 40);
  //quit buttom implimentation
  textFont(font, 30);
  text("QUIT", 540, 30);
  if (mouseX>=500 && mouseX<=580 && mouseY>=5 && mouseY<=45) {
    strokeWeight(3);
    stroke(255,243,106);
    noFill();
    rect(540,25,80,40);
    strokeWeight(1);
    stroke(0);
  } else {
    noFill();
    strokeWeight(1);
    stroke(255);
    rect(540,25,80,40);
    stroke(0);
  }
  //draw background, initialize each array individually, then initialize each array character individually  
  image(turlingtonFloor,0,50);
  if (timerW.isFinished()) {
      // Initialize one Walker
      walker[totalWalkers] = new WalkingDude();
      // Increment totalWalkers
      totalWalkers ++;
      // hit end of the array
      if (totalWalkers >= walker.length) {
        totalWalkers = 0; // Start over
        WalkerArrayFull=true;
      }
      timerW.start();
  }
  if (timerB.isFinished()) {
    // Initialize one Biker
    biker[totalBikers] = new BikerDude();
    // Increment totalBiker
    totalBikers ++;
    // If hit end of the array
    if (totalBikers >= biker.length) {
      totalBikers = 0; // Start over
      BikerArrayFull=true;
    }
    timerB.start();
  }
  if (timerG.isFinished()) {
    // Deal with Gator
    // Initialize one gator
    gator[totalGators] = new Gator();
    // Increment totalGator
    totalGators ++;
    // If we hit the end of the array
    if (totalGators >= biker.length) {
      totalGators = 0; // Start over
      GatorArrayFull=true;
    }
    timerG.start();
  }
  if(WalkerArrayFull){
    for (int i = 0; i < 50; i++ ) {
      walker[i].move();
      walker[i].display();
    }
  }
  else{
    for (int i = 0; i < totalWalkers; i++ ) {
      walker[i].move();
      walker[i].display();
    }
  }
  if(BikerArrayFull){
    for (int j = 0; j < 20; j++ ) { 
      biker[j].move();
      biker[j].display();
    }
  }
  else{
    for (int j = 0; j < totalBikers; j++ ) { 
      biker[j].move();
      biker[j].display();
    }    
  }
  if(GatorArrayFull){
    for (int k = 0; k < 20; k++ ) {
      gator[k].move();
      gator[k].display();
    }
  }
  else{
    for (int k = 0; k < totalGators; k++ ) {
      gator[k].move();
      gator[k].display();
    }
  }
  //(time/60)+30 starts time at 30 and decreased per second (framerate is 60 seconds)
  if((time/60)+30==0){
    game=false;
    end=true;
  }
 }
      //----------End Menu----------//
  void endMenu () {
   textAlign(CENTER);
   fill(255,49,8);
   textFont(font, 40);
   text("Your Score was: "+ score, 300, 200);  //print out player score
   text("Top high score: 8", 300, 300);
   text("Play Again?: Y/N", 300,400);
  }
  void keyReleased () {
      if (key == 'y' || key == 'Y') {
        menu = true;  //go to menu screen
        end = false;
        game = false;
        int totalWalkers = 0;
        int totalBikers = 0;
        int totalGators = 0;
      }
      else if (key == 'n' || key == 'N') {
        //go to ended_game_screen
        background(0);
        textAlign(CENTER);
        fill(255,49,8);
         textFont(font,50);
         text("Peace Out!!!!",300,125);
        playing=false;
      }
    }
}
class Timer {
 
  int savedTime; // When Timer started
  int totalTime; // How long Timer should last
  
  Timer(int tempTotalTime) {
    totalTime = tempTotalTime;
  }
  
  // Starting the timer
  void start() {
    // When the timer starts it stores the current time in milliseconds.
    savedTime = millis(); 
  }
  
  // The function isFinished() returns true if 'tempTotalTime' milliseconds have passed.
  boolean isFinished() { 
    // Check how much time has passed
    int passedTime = millis()- savedTime;
    if (passedTime > totalTime) {
      return true;
    } else {
      return false;
    }
  }
}
