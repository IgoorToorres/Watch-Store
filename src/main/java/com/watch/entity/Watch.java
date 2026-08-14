package com.watch.entity;

import com.watch.exception.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "watches")
public class Watch {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "brand", nullable = false, length = 100)
    private String brand;

    @Column(name = "model", nullable = false, length = 100)
    private String model;

    @Column(name = "reference", nullable = false, length = 100)
    private String reference;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 20)
    private MovementType movementType;

    @Enumerated(EnumType.STRING)
    @Column(name = "case_material", nullable = false, length = 20)
    private CaseMaterial caseMaterial;

    @Enumerated(EnumType.STRING)
    @Column(name = "crystal_type", nullable = false, length = 20)
    private CrystalType crystalType;

    @Column(name = "water_resistance_m", nullable = false)
    private Integer waterResistanceM;

    @Column(name = "diameter_mm", nullable = false)
    private Integer diameterMm;

    @Column(name = "lug_to_lug_mm", nullable = false)
    private Integer lugToLugMm;

    @Column(name = "thickness_mm", nullable = false)
    private Integer thicknessMm;

    @Column(name = "lug_width_mm", nullable = false)
    private Integer lugWidthMm;

    @Column(name = "price_in_cents", nullable = false)
    private Long priceInCents;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Watch(){}

    public Watch(
            String brand,
            String model,
            String reference,
            MovementType movementType,
            CaseMaterial caseMaterial,
            CrystalType crystalType,
            Integer waterResistanceM,
            Integer diameterMm,
            Integer lugToLugMm,
            Integer thicknessMm,
            Integer lugWidthMm,
            Long priceInCents
    ){
        validateNullName(brand, "Brand is required and cannot be blank.");
        validateNullName(model, "Model is required and cannot be blank.");
        validateNullName(reference, "Reference is required and cannot be blank.");

        validateRequired(movementType, "Movement type is required. Accepted values: QUARTZ, AUTOMATIC, MANUAL.");
        validateRequired(caseMaterial, "Case material is required. Accepted values: STEEL, TITANIUM, RESIN, BRONZE, CERAMIC.");
        validateRequired(crystalType, "Crystal type is required. Accepted values: MINERAL, SAPPHIRE, ACRYLIC.");

        validateWaterResistanceM(waterResistanceM, "Water resistance is required and cannot be negative.");
        validatePositiveInteger(diameterMm, "Diameter must be greater than zero millimeters.");
        validatePositiveInteger(lugToLugMm, "Lug-to-lug measurement must be greater than zero millimeters.");
        validatePositiveInteger(thicknessMm, "Thickness must be greater than zero millimeters.");
        validatePositiveInteger(lugWidthMm, "Lug width must be greater than zero millimeters.");

        validatePrice(priceInCents, "Price is required and must be greater than or equal to zero cents.");

        this.id = UUID.randomUUID();
        this.brand = brand;
        this.model = model;
        this.reference = reference;
        this.movementType = movementType;
        this.caseMaterial = caseMaterial;
        this.crystalType = crystalType;
        this.waterResistanceM = waterResistanceM;
        this.diameterMm = diameterMm;
        this.lugToLugMm = lugToLugMm;
        this.thicknessMm = thicknessMm;
        this.lugWidthMm = lugWidthMm;
        this.priceInCents = priceInCents;
    }

    public UUID getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getReference() {
        return reference;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public CaseMaterial getCaseMaterial() {
        return caseMaterial;
    }

    public CrystalType getCrystalType() {
        return crystalType;
    }

    public Integer getWaterResistanceM() {
        return waterResistanceM;
    }

    public Integer getDiameterMm() {
        return diameterMm;
    }

    public Integer getLugToLugMm() {
        return lugToLugMm;
    }

    public Integer getThicknessMm() {
        return thicknessMm;
    }

    public Integer getLugWidthMm() {
        return lugWidthMm;
    }

    public Long getPriceInCents() {
        return priceInCents;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    private void validateNullName(String name, String exceptionMessage){
        if(name == null || name.isBlank()){
            throw new DomainException(exceptionMessage);
        }
    }

    private void validateRequired(Object value, String exceptionMessage){
        if(value == null){
            throw new DomainException(exceptionMessage);
        }
    }

    private void validatePositiveInteger(Integer value, String exceptionMessage){
        if(value == null || value <= 0){
            throw new DomainException(exceptionMessage);
        }
    }

    private void validateWaterResistanceM(Integer waterResistanceM, String exceptionMessage) {
        if (waterResistanceM == null || waterResistanceM < 0) {
            throw new DomainException(exceptionMessage);
        }
    }

    private void validatePrice(Long priceInCents, String exceptionMessage){
        if(priceInCents == null || priceInCents < 0){
            throw new DomainException(exceptionMessage);
        }
    }

    @PrePersist
    private void onCreate(){
        LocalDateTime now = LocalDateTime.now();

        if(this.id == null){
            this.id = UUID.randomUUID();
        }

        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    private void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
