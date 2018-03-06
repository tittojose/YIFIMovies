package yifimovies.tittojose.me.yifi.homescreen.movieslist;


import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import yifimovies.tittojose.me.yifi.api.model.Movie;
import yifimovies.tittojose.me.yifi.repositories.MoviesRepository;

/**
 * Created by titto.jose on 01-03-2018.
 */
public class MoviesListPresenterTest {


    @Test
    public void shouldPassBooksToView() {
        //give
        MoviesListView view = new MockView();
        MoviesRepository mockMovieRepositories = new MockMoviesRepo(true);

        // when
        MoviesListPresenter moviesListPresenter = new MoviesListPresenter(view, mockMovieRepositories);
        moviesListPresenter.loadMovies();

        //then
        Assert.assertEquals(true, ((MockView) view).displayBooksCalled);

    }

    @Test
    public void shouldShowErrorMessageToView() {
        //give
        MoviesListView view = new MockView();
        MoviesRepository mockMovieRepositories = new MockMoviesRepo(false);

        // when
        MoviesListPresenter moviesListPresenter = new MoviesListPresenter(view, mockMovieRepositories);
        moviesListPresenter.loadMovies();

        //then
        Assert.assertEquals(true, ((MockView) view).displayErrorMessageCalled);
    }

    private class MockView implements MoviesListView {
        boolean displayBooksCalled;
        boolean displayErrorMessageCalled;

        @Override
        public void displayMoviesList(@NotNull List<? extends Movie> movies) {
            displayBooksCalled = true;
        }

        @Override
        public void displayErrorWhenNoMoviesLoaded(String errorMessage) {
            displayErrorMessageCalled = true;
        }
    }

    private class MockMoviesRepo implements MoviesRepository {


        private boolean shouldReturnMovies;

        public MockMoviesRepo(boolean shouldReturnMovies) {

            this.shouldReturnMovies = shouldReturnMovies;
        }

        @Override
        public List<Movie> getListOfMovies() {
            if (shouldReturnMovies) {
                return Arrays.asList(new Movie(), new Movie(), new Movie());
            } else {
                return Collections.emptyList();
            }
        }
    }
}