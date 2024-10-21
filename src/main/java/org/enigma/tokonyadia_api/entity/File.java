package org.enigma.tokonyadia_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "m_file")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "conten_type", nullable = false)
    private String contentType;

    @Column(name = "size", nullable = false)
    private long size;
}
