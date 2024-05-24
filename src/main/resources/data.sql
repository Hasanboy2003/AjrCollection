create or replace view view_brands_projection as
select b.id                                                 as id,
       to_char(b.created_at, 'Mon DD, YYYY, HH24:MI'::text) AS createdAt,
       to_char(b.updated_at, 'Mon DD, YYYY, HH24:MI'::text) AS updatedAt,
       b.name                                               as name,
       b.active                                             as active,
       cast(b.attachment_id as varchar)                     as logoId
from brand b;

create or replace view view_categories_projection as
select cast(c.id as varchar)                                as id,
       to_char(c.created_at, 'Mon DD, YYYY, HH24:MI'::text) AS createdAt,
       to_char(c.updated_at, 'Mon DD, YYYY, HH24:MI'::text) AS updatedAt,
       c.name                                               as name,
       c.description                                        as description,
       c.active                                             as active
from category c;

create or replace view view_distributors_projection as
select d.id                                                 as id,
       to_char(d.created_at, 'Mon DD, YYYY, HH24:MI'::text) AS createdAt,
       to_char(d.updated_at, 'Mon DD, YYYY, HH24:MI'::text) AS updatedAt,
       d.name                                               as name,
       d.description                                        as description,
       d.active                                             as active
from distributor d;

create or replace view view_incomes_projection as
select cast(i.id as varchar)                                as id,
       to_char(i.created_at, 'Mon DD, YYYY, HH24:MI'::text) AS createdAt,
       to_char(i.updated_at, 'Mon DD, YYYY, HH24:MI'::text) AS updatedAt,
       i.code                                               as incomeCode,
       i.description                                        as incomeDescription,
       coalesce(sum(id2.price), 0)                          as incomeTotalSum
from income i
         left join income_detail id2 on id2.income_id = i.id
group by i.id, i.code
order by i.code desc; -- THE LAST ADDED INCOMES ARE PRINTED FIRST

create or replace view view_colors_projection as
select c.id                                                 as id,
       to_char(c.created_at, 'Mon DD, YYYY, HH24:MI'::text) AS createdAt,
       to_char(c.updated_at, 'Mon DD, YYYY, HH24:MI'::text) AS updatedAt,
       c.name                                               as name,
       c.code                                               as code
from color c
order by c.name;

create or replace view view_discounts_projection as
select d.id                                                 as id,
       to_char(d.start_date, 'Mon DD, YYYY'::text)          AS startDate,
       to_char(d.end_date, 'Mon DD, YYYY'::text)            AS endDate,
       d.percent                                            as percent
from discount d
order by d.end_date;

create or replace view view_income_details_projection as
select cast(idl.id as varchar)                                as id,
       to_char(idl.created_at, 'Mon DD, YYYY, HH24:MI'::text) AS createdAt,
       to_char(idl.updated_at, 'Mon DD, YYYY, HH24:MI'::text) AS updatedAt,
       idl.quantity                                           as quantity,
       idl.price                                              as incomePrice,
       cast(idl.income_id as varchar)                         as incomeId
from income_detail idl;

create or replace view view_product_colors_projection as
select cast(pc.id as varchar)                                as id,
       to_char(pc.created_at, 'Mon DD, YYYY, HH24:MI'::text) AS createdAt,
       to_char(pc.updated_at, 'Mon DD, YYYY, HH24:MI'::text) AS updatedAt
from product_color as pc;

create or replace view view_products_projection as
SELECT p.id::character varying                                                          AS id,
       to_char(p.created_at, 'Mon DD, YYYY, HH24:MI'::text)                             AS createdAt,
       to_char(p.updated_at, 'Mon DD, YYYY, HH24:MI'::text)                             AS updatedAt,
       p.name                                                                           as name,
       p.description                                                                    as description,
       p.active                                                                         as active,
       p.outcome_price                                                                  AS outcomePrice,
       p.code                                                                           as code,
       d.id                                                                             AS discountId,
       case when d.id is not null and d.end_date > now()::date then true else false end as isOnDiscount,
       case when d.id is not null and d.end_date > now()::date then d.percent end       as discountPercent,
       case
           when d.id is not null and d.end_date > now()::date
               then p.outcome_price - (p.outcome_price * d.percent / 100) end           as discountPrice
FROM product p
         LEFT JOIN discount d on d.id = p.discount_id
ORDER BY p.code DESC;

create or replace view view_sizes_projection as
select s.id                                                 as id,
       to_char(s.created_at, 'Mon DD, YYYY, HH24:MI'::text) AS createdAt,
       to_char(s.updated_at, 'Mon DD, YYYY, HH24:MI'::text) AS updatedAt,
       s.name                                               as name,
       s.active                                             as active
from size s;

-- select p.id                         as id,
--        p.name                       as name,
--        (
--            select pa.id
--            from product_color pc
--                     join product p2 on p2.id = pc.product_id
--                     join product_attachment pa on pc.id = pa.product_color_id
--            where p2.id = p.id
--            limit 1
--        )                            as attachmentId,
--        p.outcome_price              as price,
--        case
--            when d.id is not null and d.end_date >= now()::date then d.percent
--            end            as discount,
--        case
--            when d.id IS NOT NULL and d.end_date >= now()::date
--                then p.outcome_price - (p.outcome_price * d.percent / 100)
--            else p.outcome_price end as sellingPrice,
--        b.name                       as brandName,
--        case
--            when p.id in (
--                select uf.product_id
--                from users_favourites uf
--                where uf.user_id = :userId
--            ) then true
--            else false end           as favourite
-- from product p
--          join category c on p.category_id = c.id
--          join brand b on p.brand_id = b.id
--          left join discount d on p.discount_id = d.id
-- where lower(p.name) like concat('%', lower(:searchText), '%')
--    or p.code = :searchText
--    or lower(b.name) like concat('%', lower(:searchText), '%')
--    or lower(c.name) like concat('%', lower(:searchText), '%');