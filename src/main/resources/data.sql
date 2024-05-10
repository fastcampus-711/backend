insert into common_post (title, content, created_by, created_at, hits, visible, DTYPE)
values ('title', 'content', 'created_by', '2021-03-10 08:48:50', 30, TRUE, 'NoticePost');
insert into notice_post (id, post_at)
values (1, '2021-03-10 08:48:50');

insert into common_post (title, content, created_by, created_at, hits, visible, DTYPE)
values ('FreeBoardTitle', 'content', 'created_by', '2021-03-10 08:48:50', 30, TRUE, 'FreeBoardPost');
insert into free_board_post (id, blind_at, blind_by)
values (2, '2021-03-10 08:48:50', 'blind_by');
-- insert into free_board_post (id, title, content, created_by, created_at, hits, status)
-- values (2, 'freeBoard', 'content', 'created_by', '2021-03-10 08:48:50', 30, TRUE);