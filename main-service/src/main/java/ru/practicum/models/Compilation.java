package ru.practicum.models;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Модель объекта Compilation
 */
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "compilation_events",
            joinColumns = {@JoinColumn(name = "compilation_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")}
    )
    private Set<Event> events = new HashSet<>();
    @Column(name = "pinned")
    private boolean pinned;
    @Column(name = "title")
    private String title;
}