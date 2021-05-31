package org.zeith.mccue.api;

@FunctionalInterface
public interface EFunc<K, V>
{
	default <Q> EFunc<K, Q> map(EFunc<V, Q> f)
	{
		return k -> f.f(this.f(k));
	}

	default <Q, X> EFunc<K, X> map2(EFunc<V, Q> f1, EFunc<Q, X> f2)
	{
		return k -> f2.f(f1.f(this.f(k)));
	}

	default <Q, X, Y> EFunc<K, Y> map3(EFunc<V, Q> f1, EFunc<Q, X> f2, EFunc<X, Y> f3)
	{
		return k -> f3.f(f2.f(f1.f(this.f(k))));
	}

	default <Q, X, Y, Z> EFunc<K, Z> map4(EFunc<V, Q> f1, EFunc<Q, X> f2, EFunc<X, Y> f3, EFunc<Y, Z> f4)
	{
		return k -> f4.f(f3.f(f2.f(f1.f(this.f(k)))));
	}

	V f(K var1);
}