% Used for displaying the faces of the convex hull

faces = [18	5	19
17	18	19
2	6	1
2	4	3
11	12	6
11	6	7
13	17	10
13	10	12
13	12	15
9	6	12
9	12	10
9	1	6
8	2	3
8	15	14
14	15	12
14	12	11
14	11	7
14	7	8
18	4	5
17	16	18
2	7	6
2	1	5
2	5	4
13	15	16
13	16	17
9	10	17
9	17	19
9	19	5
9	5	1
8	7	2
8	3	4
8	4	18
8	18	16
8	16	15
];

vert = [139	141	3
139	156	2
139	181	1
139	210	1
139	210	3
143	137	3
144	151	2
155	165	1
160	123	3
170	115	3
176	123	2
183	117	2
186	115	2
203	119	1
208	115	1
230	115	1
230	115	3
230	210	1
230	210	3
];

for i = 1:4
    im(:,:,i) = imread('CUBE2.tif',i);
end
idx = find(im(:)==0);
[x,y,z] = ind2sub(size(im),idx);

figure
plot3(x,y,z*25,'.')
daspect([1 1 1]);
hold on
title(num2str(i))

verts = verts + 1;
faces = faces + 1;
vert = verts;
for i = 1:size(faces,1)
    f = faces(i,:);
    plot3(vert(f(1),2),vert(f(1),1),vert(f(1),3)*25,'go');
    plot3(vert(f(3),2),vert(f(3),1),vert(f(3),3)*25,'ro');
    line([vert(f(1),2),vert(f(2),2),vert(f(3),2)],[vert(f(1),1),vert(f(2),1),vert(f(3),1)],[vert(f(1),3)*25,vert(f(2),3)*25,vert(f(3),3)*25],'Color',[1 0 0]);
end