package com.devsuperior.movieflix.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.MovieByGenreDTO;
import com.devsuperior.movieflix.dto.MovieDTO;
import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.projections.MovieByGenreProjection;
import com.devsuperior.movieflix.repositories.GenreRepository;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.repositories.UserRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class MovieService {

	@Autowired
	private MovieRepository repository;
	
	@Autowired
	private GenreRepository genreRepository;

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private UserRepository userRepository;
		
	@Transactional(readOnly = true)
	public MovieDTO findById(Long id) {
		Optional<Movie> obj = repository.findById(id);
		Movie entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new MovieDTO(entity,entity.getGenre());
	}
	
	@Transactional(readOnly = true)
	public Page<MovieByGenreDTO> findAllPaged(Long genreId,Pageable pageable){
		Genre genre = (genreId == 0) ? null : genreRepository.getOne(genreId);
		Page<MovieByGenreProjection> page = repository.findByGenre(genre,pageable);	 
		return page.map(x -> new MovieByGenreDTO(x));
	}

	@Transactional(readOnly = true)
	public List<ReviewDTO> findAllMovieReviews(Long id){	

		Movie movie = (id == 0) ? null : repository.getOne(id);

		List<Review> list = reviewRepository.findAllMovieReviews(movie);
		
		return list.stream().map(x -> new ReviewDTO(x,x.getUser())).collect(Collectors.toList());
	}

	
}
