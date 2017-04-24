function [ pre ] = testLearning(num_labels,Theta1,Theta2,IN )
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here
    X=zeros(0,720);
    Input=fopen(IN,'r');
    Data=zeros(720);
    while ~feof(Input)
        in=fgetl(Input);
 %       disp(in);
%        pause;
        Img=imread(strcat('Images/test/',in));
  %      imshow(Img);
%        D=ImgCompress(sum(Img,3)/3,100,100);
    [m,n,c]=size(Img);
    Data=extractHOGFeatures(Img,'cellsize',int32([m/6 n/6]));
        %Data(1:2500)=reshape(double(D),1,[]);
        X=[X;Data];
    end
    disp(size(X));
    disp(size(Theta1));
    z2=[ones(size(X,1),1) X]*Theta1';
    u=1./(1+exp(-z2));
    a2=[ones(size(z2,1),1) u];
    z3=a2*Theta2';
    predict=1./(1+exp(-z3));
%    disp(predict);    
    if num_labels==1
        %singal class...
        pre=(predict>0.5);
    else
        %multiclass...
        [p,pre]=max(predict,[],2);
    end
%    disp(pre);


end