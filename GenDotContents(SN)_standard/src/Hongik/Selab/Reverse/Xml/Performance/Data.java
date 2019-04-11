package Hongik.Selab.Reverse.Xml.Performance;

public class Data {
	private String method_name;
	private float self;
	private float accum;
	private int count;
	
	public Data(String method_name, float self, float accum, int count) {
		// TODO Auto-generated constructor stub
		this.setMethod_name(method_name);
		this.setSelf(self);
		this.setAccum(accum);
		this.setCount(count);
	}

	public String getMethod_name() {
		return method_name;
	}

	public void setMethod_name(String method_name) {
		this.method_name = method_name;
	}

	public float getSelf() {
		return self;
	}

	public void setSelf(float self) {
		this.self = self;
	}

	public float getAccum() {
		return accum;
	}

	public void setAccum(float accum) {
		this.accum = accum;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
