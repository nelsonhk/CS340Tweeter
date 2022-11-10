package edu.byu.cs.tweeter.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;

public class PostStatusUnitTests {

    private MainPresenter.MainView mockView;
    private StatusService mockStatusService;
    private MainPresenter mainPresenterSpy;
    private String post = "This is a mock post";


    @BeforeEach
    public void setup() {
        mockView = Mockito.mock(MainPresenter.MainView.class);
        mockStatusService = Mockito.mock(StatusService.class);
        mainPresenterSpy = Mockito.spy(new MainPresenter(mockView));
        Mockito.doReturn(mockStatusService).when(mainPresenterSpy).getStatusService();
    }

    @Test
    public void postStatus_postSuccessful() {
        Answer answer = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                //Assertions for parameters
                Assertions.assertNotNull(invocation.getArgument(1));
                MainPresenter.PostStatusObserver observer =
                        invocation.getArgument(1, MainPresenter.PostStatusObserver.class);
                observer.postStatusSuccess();
                return null;
            }
        };

        verifyMethodCalls(answer);
        Mockito.verify(mockView).removeInfoToast();
        Mockito.verify(mockView).displayInfoToast("Successfully Posted!");
    }

    @Test
    public void postStatus_postFailed() {
        Answer answer = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Assertions.assertNotNull(invocation.getArgument(1));
                MainPresenter.PostStatusObserver observer =
                        invocation.getArgument(1, MainPresenter.PostStatusObserver.class);
                observer.handleFailure("Failure");
                return null;
            }
        };

        verifyMethodCalls(answer);
        Mockito.verify(mockView).displayErrorMessage("Failure");
    }

    @Test
    public void postStatus_postThrewException() {
        Answer answer = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Assertions.assertNotNull(invocation.getArgument(1));
                MainPresenter.PostStatusObserver observer =
                        invocation.getArgument(1, MainPresenter.PostStatusObserver.class);
                observer.handleFailure("Exception");
                return null;
            }
        };

        verifyMethodCalls(answer);
        Mockito.verify(mockView).displayErrorMessage("Exception");
    }

    private void verifyMethodCalls(Answer answer) {
        Mockito.doAnswer(answer).when(mockStatusService).postStatus(Mockito.any(), Mockito.any());
        mainPresenterSpy.postStatus(post);
        Mockito.verify(mockView).displayInfoToast("Posting Status...");
    }

}
