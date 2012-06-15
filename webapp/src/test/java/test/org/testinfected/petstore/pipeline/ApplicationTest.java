package test.org.testinfected.petstore.pipeline;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.internal.StateMachine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.Handler;
import org.testinfected.petstore.pipeline.Application;
import org.testinfected.petstore.pipeline.Middleware;

@RunWith(JMock.class)
public class ApplicationTest {

    Application application = new Application();

    Mockery context = new JUnit4Mockery();
    Handler handler = context.mock(Handler.class);
    Middleware onTop = context.mock(Middleware.class, "on top");
    Middleware inTheMiddle = context.mock(Middleware.class, "in the middle");
    Middleware atBottom = context.mock(Middleware.class, "at bottom");
    
    Request request = context.mock(Request.class);
    Response response = context.mock(Response.class);

    @Test public void
    assemblesChainInOrderOfAddition() throws Exception {
        final States chain = new StateMachine("chain");
        context.checking(new Expectations() {{
            oneOf(onTop).wrap(inTheMiddle);
            oneOf(inTheMiddle).wrap(atBottom);
            oneOf(atBottom).wrap(handler); then(chain.is("assembled"));
            
            oneOf(onTop).handle(with(same(request)), with(same(response))); when(chain.is("assembled"));
        }});
        
        application.use(onTop);
        application.use(inTheMiddle);
        application.use(atBottom);
        application.run(handler);

        application.handle(request, response);
    }
}
