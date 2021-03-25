package com.sht.vehicle.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sht.vehicle.dto.CarDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Aaron
 * @date 2021/2/15 10:44
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Car{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 汽车品牌
     */
    @ApiModelProperty(notes = "汽车品牌型号")
    private String brand;

    /**
     * 备注
     */
    @ApiModelProperty(notes = "备注")
    private String notes;

    /**
     * 限载人数
     */
    @ApiModelProperty(notes = "限载人数")
    private Integer number;

    @ApiModelProperty(notes = "车牌号")
    private String licensePlateNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "购车时间")
    private LocalDateTime buyDateTime;

    /**
     * 入库时间
     */
    @ApiModelProperty(name = "入库时间", notes = "由新增汽车时，后台自动录入")
    private LocalDate inDate;

    /**
     * //级联保存、更新、删除、刷新;延迟加载。当删除用户，会级联删除该用户的所有文章
     * //拥有mappedBy注解的实体类为关系被维护端
     * //mappedBy="author"中的author是Article中的author属性
     * @OneToMany(mappedBy = "car", targetEntity = Scheduling.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
     * private Set<Scheduling> schedulingSet = new HashSet<>();
     */
    @JsonBackReference(value = "car-refuel")
    @OneToMany(targetEntity = Refuel.class, mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Refuel> refuelSet = new HashSet<>();

    @JsonBackReference(value = "car-upkeep")
    @OneToMany(targetEntity = Upkeep.class, mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Upkeep> upkeepSet = new HashSet<>();

    /*
        @OneToMany(fetch=FetchType.)的选项有，如下图：
            FetchType.EAGER:代表立即加载；
            FetchType.LAZY:代表延迟加载。

        当我们把fetch设置为FetchType.LAZY的时候，什么时候初始化items里面的数据呢？当我们第一次访问这个属性，并对这个属性进行操作的时候，
        这个集合的数据才会从数据库里面load出来。但要注意：当我们访问这个延迟属性的时候，我们的前提要EntityManager这个对象没有被关闭，
        如果被关闭了我们再去访问延迟属性的话，就访问不到，并抛出延迟加载意外。
        如果没有设置fetch这属性的话，会怎么样呢？是立即加载？还是延迟加载呢？
        记住@OneToMany这个标签最后的英文单词,如果是要得到Many的一方，我不管你前面是什么，只要后面的单词是Many，也就是说要得到多的一方，
        你们就给我记住，默认的加载策略就是延迟加载(Many记录可能上几万条，立即加载的话可能对效率影响大，所以延迟加载)。
        反过来，如果后面是One呢？因为它是加载一的一方，这对性能影响不是很大，所以它的默认加载策略是立即加载。

        mappedBy：我们怎么知道关系的维护端和被维护端呢？当然JPA规范规定多的一端应该是为维护端（关系维护段增加一个字段为外键，里面保存的是一的一端的主键），
        一的一端为关系被维护端，那么我们总要在程序里给他们打上标志吧？虽然规范是这么规定，但总要申明一下吧？就是通过mappedBy属性，只要哪个类出现了mappedBy，
        那么这个类就是关系的被维护端。里面的值指定的是关系维护端。
        orderDetail这边由哪一个属性去维护关系呢？是OrderItem类的order属性。
        mappedBy属性对应Hibernate里面的inverse属性：<SET name="orderDetails" inverse="true"></SET>

    */

    @Transient
    private List<Refuel> refuels;

    public Car(CarDto carDto) {
        this.id = carDto.getId();
        this.brand = carDto.getBrand();
        this.notes = carDto.getNotes();
        this.number = carDto.getNumber();
        this.inDate = carDto.getInDate();
        this.licensePlateNumber = carDto.getLicensePlateNumber();
        this.buyDateTime = carDto.getBuyDateTime();
    }

}
