package com.dicka.microserviceorderservice;

import lombok.*;

@Getter
@Setter
public class GenericData<T> {

	private T t1;
	private T t2;
	
	public GenericData(T t1, T t2) {
		this.t1 = t1;
		this.t2 = t2;
	}
}
