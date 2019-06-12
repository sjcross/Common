function acc = houghCircleTransform(im,minR,maxR)
% Initialising the accumulator
acc = double(zeros([size(im),maxR-minR+1]));

% Scanning through all radii
for r=minR:maxR    
    % Generating coordinates for the circle (Jean-Yves's function)
    [x,y] = getmidpointcircle(0,0,r);
    
    % Iterating over all centroid locations, checking if the point is a
    % signal pixel
    for xx = 1:size(im,1)
        for yy = 1:size(im,2)
            %if im(xx,yy) ~= 0 %%% IF THIS IS BINARY THE ZERO PIXELS WON'T CONTRIBUTE ANYWAY
                % Getting pixel coordinates around the current pixel
                x1 = x+xx;
                y1 = y+yy;
                
                % Retaining only those pixels inside the image
                idx = find(x1>0 & x1 <=size(im,1) & y1>0 & y1<=size(im,2));
                
                % Incrementing the pixels in the accumulator
                for i=1:numel(idx)
                    acc(x1(idx(i)),y1(idx(i)),r-minR+1) = acc(x1(idx(i)),y1(idx(i)),r-minR+1) + double(im(xx,yy));
                end
            %end
        end
    end
    
    % Normalising the angle based on the number of points in a circle at
    % this radius
    acc(:,:,r-minR+1) = acc(:,:,r-minR+1)./numel(x);
    
end
