package com.borrogg.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "file")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Integer fileId;

    @Column(name = "name")
    private String name;

    @Column(name = "format")
    private String format;

    @Column(name = "date_download")
    private LocalDate dateDownload;

    @Column(name = "size_kb")
    private Integer sizeKB;

    @Column(name = "file_code")
    private String fileCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

//    @Column(name = "client_id", insertable = false, updatable = false)
//    private Integer clientId;
}
