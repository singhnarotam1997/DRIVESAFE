function [ Theta1, Theta2 ] = Learning( num_labels, label, Limit, limit )
%UNTITLED3 Summary of this function goes here
%   Detailed explanation goes here
fid=fopen('Images/file.txt','r');
DataLine=fgetl(fid);
FileSize=[];
y=[];
DataFile=char(Limit,30);
i=1;
DataNum=[0 0 0 0 0 0 0 0 0 0];
while ischar(DataLine)
    n=size(DataLine,2);
    if DataNum(DataLine(4)-'0'+1)>=limit(DataLine(4)-'0'+1)
        DataLine=fgetl(fid);
        continue;
    else
        DataNum(DataLine(4)-'0'+1)=DataNum(DataLine(4)-'0'+1)+1;
    end
    FileSize=[FileSize n-2];
    if num_labels==1
 %   for singal class...
        if DataLine(4)=='0'
            y=[y;0];
        else
            y=[y;1];
        end
    else
        y=[y;label(DataLine(4)-'0'+1)];
    end
    DataFile(i,1:n-2)=DataLine(3:n);
    i=i+1;
    if i>Limit
        break;
    end
    DataLine=fgetl(fid);
end
%disp(DataFile(1:10,:));
%disp(size(DataFile));
disp(y);
disp(size(y));
fprintf('File names are been readed....\n');
pause;
X=zeros(Limit,2501);%,10001);
Size=min(size(FileSize,2),Limit);
for i=1:Size
    In=imread(strcat('Images/train/',DataFile(i,1:FileSize(i))));
    D=ImgCompress(sum(In,3)/3,50,50);
    %imshow(uint8(D));
%   disp(D);
    [m,n,c]=size(In);
%    Data=extractHOGFeatures(In,'cellsize',int32([m/6 n/6]));
    Data=reshape(double(D),1,[]);
  %  D=rgb2gray(In);
   % [m,n]=size(D);
    %points=detectSURFFeatures(D);
%    points=(points.selectStrongest(10));
    %Data
%   disp(size(Data));
%    subplot(1,2,1);
%    imshow(In);
%    subplot(1,2,2);
%   imshow(uint8(D));
%    disp(size(Data));
%    pause;
    X(i,2:size(Data,2)+1)=Data;
    clc;
    fprintf('Done:%d\n',i);
    %return;
end
X(:,1)=1;
%    load('ML/ex4data1.mat');
    options = optimset('MaxIter', 3000);
%  You should also try different values of lambda
    lambda = 1;
    disp(size(X));
    input_layer_size=size(X,2)-1;
    hidden_layer_size=100;
    initial_Theta1 = randInitializeWeights(input_layer_size, hidden_layer_size);
    initial_Theta2 = randInitializeWeights(hidden_layer_size, num_labels);
% Unroll parameters
    initial_nn_params = [initial_Theta1(:) ; initial_Theta2(:)];
    disp('Starting.....');
% Create "short hand" for the cost function to be minimized
    costFunction = @(p) nnCostFunction(p, ...
                                   input_layer_size, ...
                                   hidden_layer_size, ...
                                   num_labels, X, y, lambda);
    disp('Starting.....');
% Now, costFunction is a function that takes in only one argument (the
% neural network parameters)
disp(size(y));
disp(size(X));
    [nn_params, cost] = fmincg(costFunction, initial_nn_params, options);
    disp(cost);
    
% Obtain Theta1 and Theta2 back from nn_params
    Theta1 = reshape(nn_params(1:hidden_layer_size * (input_layer_size + 1)), ...
                 hidden_layer_size, (input_layer_size + 1));

    Theta2 = reshape(nn_params((1 + (hidden_layer_size * (input_layer_size + 1))):end), ...
                 num_labels, (hidden_layer_size + 1));
    fprintf('Completed');
    
    disp(size(X));
    disp(size(Theta1));
    z2=X*Theta1';
    u=1./(1+exp(-z2));
    a2=[ones(size(z2,1),1) u];
    z3=a2*Theta2';
    predict=1./(1+exp(-z3));
    
    if num_labels==1
        %singal class...
        pre=(predict>0.5);
    else
        %multiclass...
        [p,pre]=max(predict,[],2);
    end
    for i=1:1000:9001
        disp([pre(i:i+999) y(i:i+999)]);
        pause;
    end
    Acc=sum(abs(pre==y))/size(y,1);
    fprintf('Accuracy:%f\n',Acc);
%     fprintf('Program paused. Press enter to continue.\n');
%     pause;
%     pred = predict(Theta1, Theta2, X);
%     fprintf('\nTraining Set Accuracy: %f\n', mean(double(pred == y)) * 100);
%     fprintf('Program paused. Press enter to continue.\n');
%     pause;
%disp(Theta1);
%disp(Theta2);
%    Theta=[Theta1 Theta2];
end