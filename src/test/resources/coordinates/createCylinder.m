clear

im = zeros(76,64,60);

% Creating the circle in 2D for all slices
xCent = 30;
zCent = 25;
r = 12;

for z = 1:size(im,3)
    for x = 1:size(im,2)
        for y=1:size(im,1)
            dist = sqrt((x-xCent)^2+(z-zCent)^2);
            if dist < r
                im(y,x,z) = 255;
            end
        end
    end
end

% Only storing every 5th slice
im2 = im(:,:,1:5:end);
imwrite(im2(:,:,1),'cylinder.tif');
for i=2:size(im2,3)
    imwrite(im2(:,:,i),'cylinder.tif','writeMode','append');
end