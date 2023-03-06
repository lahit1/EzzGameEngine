package com.Ezz.Game.Engine.math;

import android.annotation.NonNull;
import com.Ezz.Game.Engine.math.MathUtils;

public class Vector {

    public float x = 0;
    public float y = 0;

    public Vector() {}

    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector(Vector reference) {
        this.x = reference.x;
        this.y = reference.y;
    }

    public float getHypotenuse() {
        return (float)Math.hypot(x, y);
    }

    public float getAngle() {
        float angle = (float) Math.atan2(y, x) * MathUtils.radiansToDegrees;
        if (angle < 0) angle += 360;
        return angle;
    }

    public float getAngle(Vector reference) {
        float x= Math.max(this.x, reference.x) - Math.min(this.x, reference.x);
        float y= Math.max(this.y, reference.y) - Math.min(this.y, reference.y);
        float angle = (float) Math.atan2(y, x) * MathUtils.radiansToDegrees;
        if (angle < 0) angle += 360;
        return angle;
    }

    public float distance() {
        return (float)Math.hypot(x, y);
    }

    public float distance(Vector reference) {
        float x = this.x - reference.x;
        float y = this.y - reference.y;
        return (float)Math.hypot(x, y);
    }

	public void reset() {
		x = 0;
		y = 0;
	}

	public boolean isZero() {
		return x == 0 && y == 0;
	}

    public Vector set(float x, float y) {
        this.x = x;
        this.y = y;
		return this;
    }

    public Vector set(Vector reference) {
        x = reference.x;
        y = reference.y;
		return this;
    }

    public Vector add(float x, float y) {
        this.x += x;
        this.y += y;
		return this;
    }

    public Vector add(Vector reference) {
        this.x += reference.x;
        this.y += reference.y;
		return this;
    }

    public Vector sub(float x, float y) {
        this.x -= x;
        this.y -= y;
		return this;
    }

    public Vector sub(Vector reference) {
        this.x -= reference.x;
        this.y -= reference.y;
		return this;
    }

    public Vector scl(float scl) {
        this.x *= scl;
        this.y *= scl;
		return this;
    }

	public Vector mulAdd(Vector v, float m) {
        this.x += v.x * m;
        this.y += v.y * m;
		return this;
    }

	public Vector rotate (float degrees) {
		return rotateRad(degrees * MathUtils.degreesToRadians);
	}

	public Vector rotate(Vector reference, float degrees) {
		return this.sub(reference).rotate(degrees).add(reference);
	}

	public Vector rotateRad (float radians) {
		float cos = (float)Math.cos(radians);
		float sin = (float)Math.sin(radians);

		float newX = this.x * cos - this.y * sin;
		float newY = this.x * sin + this.y * cos;

		this.x = newX;
		this.y = newY;

		return this;
	}

	public Vector setAngle(Vector ref, float degrees) {
		float l = sub(ref).distance(ref);
		set(l, 0);
		rotate(degrees);
		add(ref);
		return this;
	}

	public Vector setAngle(float degrees) {
		float l = distance();
		set(l, 0);
		rotate(degrees);
		return this;
	}

	public Vector setAngleRad(float radians) {
		float l = distance();
		set(l, 0);
		rotateRad(radians);
		return this;
	}

    public void parseArray(float[] array) {
        x = array[0];
        y = array[1];
    }

    public Float[] toArray() {
        Float[] f = {x, y};
        return f;
    }

    public void parseString(String data) {
        String[] sa = data.split(",");
        x = Float.parseFloat(sa[0]);
        y = Float.parseFloat(sa[1]);
    }

    public boolean equals(Vector v) {
        return x == v.x && y == v.y;
    }

    public boolean equals(float x, float y) {
        return this.x == x && this.y == y;
    }

	public void lerp(Vector target, float alpha) {
		x += (target.x - x) * alpha;
		y += (target.y - y) * alpha;
	}
	
	public void translate(Vector target, float v) {
		float x = (target.x - this.x),
		y = (target.y - this.y),
		hip = (float)Math.hypot(x, y);
		
		if(hip == 0) return;
		
		v = Math.min(v, hip);
		
		this.x += x / hip * v;
		this.y += y / hip * v;
	}

    @Override
    public String toString() {
        return String.format("%f,%f", x, y);
    }
	
	public String toString(String format) {
		return format.replace("$x", Float.toString(x)).replace("$y", Float.toString(y));
	}
}
