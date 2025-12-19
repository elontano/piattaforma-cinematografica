package it.unical.dimes.mapper;

import it.unical.dimes.model.SortBy;
import it.unical.dimes.protocol.SortByDTO;

public class SortByMapper {

    public static SortBy fromGrpc(SortByDTO sortByDTO){
        switch (sortByDTO){

            case TITLE -> {
                return SortBy.TITLE;
            }
            case YEAR -> {
                return SortBy.YEAR;
            }
            case RATING -> {
                return SortBy.RATING;
            }
            default-> {
                return SortBy.NONE;
            }
        }
    }

    public static SortByDTO toGrpc(SortBy sortBy){
        switch (sortBy){

            case TITLE -> {
                return SortByDTO.TITLE;
            }
            case YEAR -> {
                return SortByDTO.YEAR;
            }
            case RATING -> {
                return SortByDTO.RATING;
            }
            default-> {
                return SortByDTO.NONE;
            }
        }
    }
}
