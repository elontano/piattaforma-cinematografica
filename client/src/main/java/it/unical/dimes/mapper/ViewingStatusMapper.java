package it.unical.dimes.mapper;

import it.unical.dimes.model.ViewingStatus;
import it.unical.dimes.protocol.ViewingStatusDTO;

public class ViewingStatusMapper {

    public static ViewingStatus fromGrpc (ViewingStatusDTO viewingStatusDTO){
        switch (viewingStatusDTO) {
            case WATCHING -> {
                return ViewingStatus.WATCHING;
            }
            case TO_WATCH -> {
                return ViewingStatus.TO_WATCH;
            }
            case WATCHED -> {
                return ViewingStatus.WATCHED;
            }
            default -> {
                return ViewingStatus.UNKNOWN_STATUS;
            }
        }
    }

    public static ViewingStatusDTO toGrpc (ViewingStatus viewingStatus){
        switch (viewingStatus){
            case WATCHING -> {
                return ViewingStatusDTO.WATCHING;
            }
            case TO_WATCH -> {
                return ViewingStatusDTO.TO_WATCH;
            }
            case WATCHED -> {
                return ViewingStatusDTO.WATCHED;
            }
            default -> {
                return ViewingStatusDTO.UNKNOWN_STATUS;
            }
        }
    }

}
