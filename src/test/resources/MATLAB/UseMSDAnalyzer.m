clear

data = csvread('C:\Users\steph\Documents\Java Projects\Common\src\test\resources\coordinates\Tracks2D.csv');
tracks = {};
un = unique(data(:,1));
for i = 1:numel(un)
    rows = find(data(:,1)==un(i));
    track = data(rows,[7,3,4]);
    tracks{i} = track;
end

analyzer = msdanalyzer(2,'um','frames');
analyzer = analyzer.addAll(tracks);
analyzer = analyzer.computeMSD;

meanMSD = analyzer.getMeanMSD;