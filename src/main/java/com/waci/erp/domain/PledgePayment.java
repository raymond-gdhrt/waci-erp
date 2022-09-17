package com.waci.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PledgePayment.
 */
@Entity
@Table(name = "pledge_payment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PledgePayment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Float amount;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "member_name", nullable = false)
    private String memberName;

    @ManyToOne
    @JsonIgnoreProperties(value = { "program", "pledgePayments" }, allowSetters = true)
    private Pledge pledge;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PledgePayment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getAmount() {
        return this.amount;
    }

    public PledgePayment amount(Float amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public PledgePayment date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public PledgePayment memberName(String memberName) {
        this.setMemberName(memberName);
        return this;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Pledge getPledge() {
        return this.pledge;
    }

    public void setPledge(Pledge pledge) {
        this.pledge = pledge;
    }

    public PledgePayment pledge(Pledge pledge) {
        this.setPledge(pledge);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PledgePayment)) {
            return false;
        }
        return id != null && id.equals(((PledgePayment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PledgePayment{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", date='" + getDate() + "'" +
            ", memberName='" + getMemberName() + "'" +
            "}";
    }
}
