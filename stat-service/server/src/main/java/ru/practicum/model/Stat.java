package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stats")
public class Stat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "app", nullable = false, length = 250)
    private String app;
    @Column(name = "uri", nullable = false, length = 250)
    private String uri;
    @Column(name = "ip", nullable = false, length = 20)
    private String ip;
    @Column(name = "timestamp")
    private LocalDateTime timeStamp;
}