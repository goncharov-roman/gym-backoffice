package org.roman.petresearch.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.roman.petresearch.dto.TrainingDto;
import org.roman.petresearch.entity.Training;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainingMapper {

    TrainingDto toDto(Training training);

    @Mapping(target = "id", ignore = true)
    Training toEntity(TrainingDto trainingDto);

    List<TrainingDto> toDtoList(List<Training> trainings);
} 