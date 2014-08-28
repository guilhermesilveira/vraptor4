package br.com.caelum.vraptor.controller;

import java.util.function.Consumer;

import javax.inject.Inject;

import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.proxy.MethodInvocation;
import br.com.caelum.vraptor.proxy.Proxifier;

/** 
 * Non thread safe java 8 linker
 */
public class Routes {

	private final Proxifier proxifier;
	private final Router router;

	/** @protected cdi only */
	Routes() {
		this(null, null);
	}

	@Inject
	public Routes(Proxifier proxifier, Router router) {
		this.proxifier = proxifier;
		this.router = router;
	}

	String result = null;

	public <T> String to(Class<T> type, Consumer<T> methodCall) {
		MethodInvocation<T> handler = (proxy, method, args, superMethod) -> {
			result = router.urlFor(type, method, args);
			return null;
		};
		T instance = (T) proxifier.proxify(type, handler);
		methodCall.accept(instance);
		return result;
	}
}