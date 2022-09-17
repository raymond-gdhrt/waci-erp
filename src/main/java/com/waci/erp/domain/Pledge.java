package com.waci.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pledge.
 */
@Entity
@Table(name = "pledge")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Pledge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
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
    @JsonIgnoreProperties(value = { "pledges" }, allowSetters = true)
    private Program program;

    @OneToMany(mappedBy = "pledge")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pledge" }, allowSetters = true)
    private Set<PledgePayment> pledgePayments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pledge id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getAmount() {
        return this.amount;
    }

    public Pledge amount(Float amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Pledge date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public Pledge memberName(String memberName) {
        this.setMemberName(memberName);
        return this;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Program getProgram() {
        return this.program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Pledge program(Program program) {
        this.setProgram(program);
        return this;
    }

    public Set<PledgePayment> getPledgePayments() {
        return this.pledgePayments;
    }

    public void setPledgePayments(Set<PledgePayment> pledgePayments) {
        if (this.pledgePayments != null) {
            this.pledgePayments.forEach(i -> i.setPledge(null));
        }
        if (pledgePayments != null) {
            pledgePayments.forEach(i -> i.setPledge(this));
        }
        this.pledgePayments = pledgePayments;
    }

    public Pledge pledgePayments(Set<PledgePayment> pledgePayments) {
        this.setPledgePayments(pledgePayments);
        return this;
    }

    public Pledge addPledgePayment(PledgePayment pledgePayment) {
        this.pledgePayments.add(pledgePayment);
        pledgePayment.setPledge(this);
        return this;
    }

    public Pledge removePledgePayment(PledgePayment pledgePayment) {
        this.pledgePayments.remove(pledgePayment);
        pledgePayment.setPledge(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pledge)) {
            return false;
        }
        return id != null && id.equals(((Pledge) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pledge{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", date='" + getDate() + "'" +
            ", memberName='" + getMemberName() + "'" +
            "}";
    }
}
