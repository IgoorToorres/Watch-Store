package com.watch.mapper;

import com.watch.dto.WatchRequest;
import com.watch.dto.WatchResponse;
import com.watch.entity.CaseMaterial;
import com.watch.entity.CrystalType;
import com.watch.entity.MovementType;
import com.watch.entity.Watch;
import org.springframework.stereotype.Component;

@Component
public class WatchMapper {

    public Watch toEntity(WatchRequest request) {
        return new Watch(
                request.brand(),
                request.model(),
                request.reference(),
                MovementType.fromApi(request.movementType()),
                CaseMaterial.fromApi(request.caseMaterial()),
                CrystalType.fromApi(request.crystalType()),
                request.waterResistanceM(),
                request.diameterMm(),
                request.lugToLugMm(),
                request.thicknessMm(),
                request.lugWidthMm(),
                request.priceInCents()
        );
    }

    public WatchResponse toResponse(Watch watch) {
        return new WatchResponse(
                watch.getId(),
                watch.getBrand(),
                watch.getModel(),
                watch.getReference(),
                watch.getMovementType().toApi(),
                watch.getCaseMaterial().toApi(),
                watch.getCrystalType().toApi(),
                watch.getWaterResistanceM(),
                watch.getDiameterMm(),
                watch.getLugToLugMm(),
                watch.getThicknessMm(),
                watch.getLugWidthMm(),
                watch.getPriceInCents(),
                calculateWaterResistanceLabel(watch),
                calculateCollectorScore(watch),
                watch.getCreatedAt(),
                watch.getUpdatedAt()
        );
    }

    private String calculateWaterResistanceLabel(Watch watch) {
        Integer waterResistanceM = watch.getWaterResistanceM();

        if (waterResistanceM < 50) {
            return "respingos";
        }

        if (waterResistanceM < 100) {
            return "uso_diario";
        }

        if (waterResistanceM < 200) {
            return "natacao";
        }

        return "mergulho";
    }

    private Integer calculateCollectorScore(Watch watch) {
        int score = 0;

        if (watch.getCrystalType() == CrystalType.SAPPHIRE) {
            score += 25;
        }

        if (watch.getWaterResistanceM() >= 100) {
            score += 15;
        }

        if (watch.getWaterResistanceM() >= 200) {
            score += 10;
        }

        if (watch.getMovementType() == MovementType.AUTOMATIC) {
            score += 20;
        }

        if (watch.getCaseMaterial() == CaseMaterial.STEEL) {
            score += 10;
        }

        if (watch.getCaseMaterial() == CaseMaterial.TITANIUM) {
            score += 12;
        }

        if (watch.getDiameterMm() >= 38 && watch.getDiameterMm() <= 42) {
            score += 8;
        }

        return score;
    }
}
