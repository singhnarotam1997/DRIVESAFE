function [ Result ] = ImgCompress( Img, SizeRow, SizeCol )
%UNTITLED2 Summary of this function goes here
%   Detailed explanation goes here
    [m,n,c]=size(Img);
    Result=zeros(int32(SizeRow),int32(n/SizeCol),c);
    FactRow=int32(m/SizeRow);
    FactCol=int32(n/SizeCol);
%    disp(FactRow);
%    disp(FactCol);
%    disp(n);
%    disp(m);
    for i=1:SizeRow
        for j=1:SizeCol
            temp=sum(sum(Img(i*FactRow:min(m,i*FactRow+FactRow-1),j*FactCol:min(n,j*FactCol+FactCol-1),:),2),1);
            Result(i,j,:)=temp/double(FactRow*FactCol);
        end
    end
end