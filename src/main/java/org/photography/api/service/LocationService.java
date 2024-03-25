package org.photography.api.service;

import org.modelmapper.ModelMapper;
import org.photography.api.dto.LocationDTO;
import org.photography.api.exception.EntityNotFoundException;
import org.photography.api.model.Location;
import org.photography.api.model.PhotoLibrary;
import org.photography.api.repository.LocationRepository;
import org.photography.api.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Autowired
    private ModelMapper modelMapper;

    public LocationDTO createLocation(LocationDTO locationDTO) {
        validateLocationName(locationDTO.getLocationName());

        Location locationToCreate = modelMapper.map(locationDTO, Location.class);

        Location createdLocation = locationRepository.save(locationToCreate);

        return modelMapper.map(createdLocation, LocationDTO.class);
    }

    private void validateLocationName(String locationName) {
        ValidationUtils.validateName(locationName);

        if (locationRepository.existsByLocationName(locationName)) {
            throw new IllegalArgumentException("Tag with the same name already exists");
        }
    }

    public LocationDTO getLocationById(Long id) {
        ValidationUtils.validateId(id);

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location", id));

        return modelMapper.map(location, LocationDTO.class);
    }

    public Set<LocationDTO> getAllLocations() {
        Set<Location> locations = new HashSet<>(locationRepository.findAll());

        return locations.stream()
                .map(location -> modelMapper.map(location, LocationDTO.class))
                .collect(Collectors.toSet());
    }

    public LocationDTO updateLocation(Long id, LocationDTO updatedLocationDTO) {
        ValidationUtils.validateId(id);
        ValidationUtils.validateName(updatedLocationDTO.getLocationName());

        Location existingLocation = locationRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Location", id));

        existingLocation.setLocationName(updatedLocationDTO.getLocationName());

        Location updatedLocation = locationRepository.save(existingLocation);

        return modelMapper.map(updatedLocation, LocationDTO.class);

    }

    @Transactional
    public void deleteLocation(Long id) {
        ValidationUtils.validateId(id);

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location", id));

        Set<PhotoLibrary> photoLibraries = location.getPhotoLibraries();
        if (!photoLibraries.isEmpty()) {
            throw new IllegalStateException("La localisation est utilisée par d'autres photos et ne peut pas être supprimée.");
        }

        locationRepository.deleteById(id);
    }

    public Location createLocationIfNotExistsOrGet(String locationName) {
        Location existingLocation = locationRepository.findByLocationName(locationName);

        if (existingLocation != null) {
            return modelMapper.map(existingLocation, Location.class);
        } else {
            Location newLocation = new Location();
            newLocation.setLocationName(locationName);
            return modelMapper.map(locationRepository.save(newLocation), Location.class);
        }
    }

}
