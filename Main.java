package main;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import shiffman.box2d.Box2DProcessing;

import java.util.HashMap;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Main extends PApplet {

	private Box2DProcessing b2d;
	private Player player;
	private Floor floor;
	
	private int KEY_A = 0;
	private int KEY_D = 0;
	
	public void setup() {
		
		b2d = new Box2DProcessing(this);
		b2d.createWorld();
		b2d.setGravity(0, -300);
		
		player = new Player(width/2, height/2, 50, 100);
		floor = new Floor(width/2, height-10, width, 20);
	}
	
	public void draw() {
		background(0);
		b2d.step();
		
		player.update();
		floor.update();
		
		player.render();
		floor.render();
	}
	
	
	private class Player {
		
		float x, y;
		float w, h;
		
		Body b;
		PolygonShape s;
		
		public Player(float x_, float y_, float w_, float h_) {
			
			x = x_;
			y = y_;
			w = w_;
			h = h_;
			
			createPlayer();
		}
		
		private void createPlayer() {
			
			BodyDef bd = new BodyDef();
			bd.position.set(b2d.coordPixelsToWorld(x, y));
			bd.type = BodyType.DYNAMIC;
			bd.bullet = true;
			
			b = b2d.createBody(bd);
			
			s = new PolygonShape();
			s.setAsBox(b2d.scalarPixelsToWorld(w/2), b2d.scalarPixelsToWorld(h/2));
			
			FixtureDef fd = new FixtureDef();
			fd.shape = s;
			
			fd.density = 5;
			fd.friction = 0.1f;
			fd.restitution = 0f;
			
			b.createFixture(fd);
		}
		
		public void update() {
			float dir = (-KEY_A + KEY_D) * 50000;
			if(dir > 0 && b.getLinearVelocity().x < 20) {
			b.applyForceToCenter(new Vec2(dir, 0));
			}
			if(dir < 0 && b.getLinearVelocity().x > -20) {
				b.applyForceToCenter(new Vec2(dir, 0));
			}
			
			
			
			PVector pos = b2d.coordWorldToPixelsPVector(b.getPosition());
			x = pos.x;
			y = pos.y;
			
			b.setTransform(new Vec2(b2d.coordPixelsToWorld(new Vec2(x, y))), 0);
		}
		
		public void jump() {
			b.applyForceToCenter(new Vec2(0, 10000000));
		}
		
		public void render() {
			pushMatrix();
			translate(x, y);
			rotate(b.getAngle());
			fill(255, 0, 0);
			rectMode(CENTER);
			rect(0, 0, w, h);
			popMatrix();
		}
	}
	
	private class Floor {
		
		float x, y;
		float w, h;
		
		Body b;
		PolygonShape s;
		
		public Floor(float x_, float y_, float w_, float h_) {
			
			x = x_;
			y = y_;
			w = w_;
			h = h_;
			
			createFloor();
		}
		
		private void createFloor() {
			
			BodyDef bd = new BodyDef();
			bd.position.set(b2d.coordPixelsToWorld(x, y));
			bd.type = BodyType.STATIC;
			
			b = b2d.createBody(bd);
			
			s = new PolygonShape();
			s.setAsBox(b2d.scalarPixelsToWorld(w/2), b2d.scalarPixelsToWorld(h/2));
			
			FixtureDef fd = new FixtureDef();
			fd.shape = s;
			
			fd.density = 5;
			fd.friction = 1;
			fd.restitution = 0f;
			
			b.createFixture(fd);
		}
		
		public void update() {
			PVector pos = b2d.coordWorldToPixelsPVector(b.getPosition());
			x = pos.x;
			y = pos.y;
		}
		
		public void render() {
			pushMatrix();
			translate(x, y);
			rotate(b.getAngle());
			fill(255);
			rectMode(CENTER);
			rect(0, 0, w, h);
			popMatrix();
		}
	}
	
	public void keyPressed() {
		switch(key) {
		case 'a':
			KEY_A = 1;
			break;
		case 'd':
			KEY_D = 1;
			break;
		case ' ':
			player.jump();
			break;
		}
	}
	
	public void keyReleased() {
		switch(key) {
		case 'a':
			KEY_A = 0;
			break;
		case 'd':
			KEY_D = 0;
			break;
		}
	}
	
	
	public void settings() {  size(800, 600); }
	  static public void main(String[] passedArgs) {
	    String[] appletArgs = new String[] { "main.Main" };
	    if (passedArgs != null) {
	      PApplet.main(concat(appletArgs, passedArgs));
	    } else {
	      PApplet.main(appletArgs);
	    }
	  }
}
