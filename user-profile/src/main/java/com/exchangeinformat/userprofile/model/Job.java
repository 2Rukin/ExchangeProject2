package com.exchangeinformat.userprofile.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@EqualsAndHashCode
@ToString

@Table(name="jobs")
public class Job {

    @Id
    @Column(name = "job_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="title")
    private String title;

    @Column(name="position")
    private String position;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name="jobs_address", joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name= "address_id"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Address> address;

    @JsonIgnore
    @OneToOne(mappedBy = "job")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

}
