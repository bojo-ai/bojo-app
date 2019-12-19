----------------------
--- TABLES - AUTHOR --
----------------------
INSERT INTO public.author (author_id, bio, created_at, name, slug, updated_at) VALUES ('wVE8Y7BoRKCBkxs1JkqBvw', null, '2016-11-14 01:14:02.096776', 'Boris Johnson', 'boris-johnson', '2016-11-14 01:14:02.096776');

-----------------------------
--- TABLES - QUOTE_SOURCE ---
-----------------------------
INSERT INTO public.quote_source (created_at, filename, quote_source_id, remarks, updated_at, url) VALUES ('2019-12-15 00:00:00.000000', null, 'in8rU9GMRxeeweuLiqz9yg', 'The Big Book of Boris', '2019-12-15 00:00:00.000000', 'https://www.dailymail.co.uk');

----------------------
--- TABLES - QUOTE ---
----------------------
INSERT INTO public.quote (appeared_at, author_id, created_at, quote_id, quote_source_id, updated_at, value) VALUES ('2003-07-22 00:00:00.000000', 'wVE8Y7BoRKCBkxs1JkqBvw', '2019-12-15 00:00:00.000000', 'c0D_6QSvTdC8t95ALENRBg', 'in8rU9GMRxeeweuLiqz9yg', '2019-12-15 00:00:00.000000', 'I have as much chance of becoming Prime Minister as of being decapitated by a frisbee or of finding Elvis.');

-------------------
--- TABLES - TAG --
-------------------
INSERT INTO public.tag (created_at, updated_at, tag_id, value) VALUES ('2016-11-20 01:32:02.227494','2016-11-20 01:32:02.227494', 'c1dIgMTURW-LllIZWSBESa', 'Music');
INSERT INTO public.tag (created_at, updated_at, tag_id, value) VALUES ('2016-11-20 01:32:02.227494','2016-11-20 01:32:02.227494', 'iI1rLZMjS4kN_XDIva9IMA', 'Elvis');

-------------------------
--- TABLES - TAG_QUOTE --
-------------------------
INSERT INTO public.quote_tag (tag_id, quote_id) VALUES ('c1dIgMTURW-LllIZWSBESa', 'c0D_6QSvTdC8t95ALENRBg');
INSERT INTO public.quote_tag (tag_id, quote_id) VALUES ('iI1rLZMjS4kN_XDIva9IMA', 'c0D_6QSvTdC8t95ALENRBg');